# `clerk` Motivation and Design Overview

`clerk` is the result of experimental work requiring management of concurrent data collection. The primary challenge revolves around scalability of data sources. Dynamically managing multiple data sources, even in a small codebase, becomes tedious. These problems are tolerable when the data is dumped to disk. However, isolating errors becomes very difficult with the development of online processing. As a result, I re-wrote my code to be modular. I can switch around components and change settings freely. With full control of the pipeline, testing and identifying errors is much easier.

What I did was break the data collection into functional pieces. Typically, we think of data collection as the consumer-side activity of data storage. Even with multiple kinds of data, handling this offline is easy. When we try to move to concurrent handling, we have a breakdown. Serious design considerations need to be made about synchronization. Depending on our design, we can end up with monolith or hard-to-modify codebases. This isn't ideal; even small changes to code can be tough to debug.

The best way to think about concurrent data collection is a case of the [producer-consumer problem](https://en.wikipedia.org/wiki/Producer%E2%80%93consumer_problem). Many data streams are going to be piped into our storage system and multiple users may request the stored data. We should be able to change the backend depending on the environment or switch to a different output.  For me, the natural choice is to synchronize on committing and reading data. Defining these two functions allows for manual control of storage.

There is also producer-side activity that we take for granted. First, data sources should be reduceable to a single blob of information. The shape of data is something we have knowledge of ahead of time. Second, we control the collection device. Separating the sources and collection lets us design general strategies. If you think of a camera as a real world example of a data collector, the image can be collected at anytime. The only limiting factors is when the film is ready. Since we control that with the rest of the camera, we can decide when data is placed from the source into storage.

Now that we have this notion of sources, collectors, and storage, we can develop our system so that the three components have no knowledge of each other's details. This reduces the development to handling the data.

## The Stopwatch

I'll provide a motivating example: a [`Stopwatch`](https://guava.dev/releases/19.0/api/docs/com/google/common/base/Stopwatch.html). `Java` has no built-in stopwatch for some reason. Luckily, it's easy to implement:

```java
public Duration profile(Runnable code) {
  Instant start = Instant.now();
  code.run();
  Instant end = Instant.now();
  return Duration.between(start, end);
}
```

This is a quick solution and works on any workload. Let's add a new data source to our profiler and put it in a class:

```java
public final class Profiler {
  private final long memStart;
  private final long memEnd;

  private Instant timeStart;
  private Instant timeEnd;

  public void start() {
    timeStart = Instant.now();
    memStart = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
  }

  public void stop() {
    timeEnd = Instant.now();
    memEnd = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
  }

  public Pair<Duration, Long> read() {
    return new Pair<>(Duration.between(timeStart, timeEnd), memEnd - memStart);
  }
}
```

This doesn't scale well; if we want to change anything about the data, we'll have to update code in 4 places. If we wanted to switch to a concurrent collection strategy (so that the memory data is actually useful), we have to make it thread-safe:

```java
public final class Profiler {
  private final Executor executor = newSingleThreadScheduledExecutor();
  private final ArrayList<Long> memoryData = new ArrayList<>();

  private Instant start;
  private Instant end;

  private boolean done = false;

  public void start() {
    done = false;
    start = Instant.now();
    executor.execute(() -> {
      while (!done) {
        memoryData.add(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory());
        try {
          Thread.sleep(1000);
        } catch (Exception e) {
          break;
        }
      }
    });
  }

  public void stop() {
    end = Instant.now();
    done = true;
  }

  public Pair<Duration, Long> read() {
    long max = 0;
    for (int i = 0; i < memoryData.size(); i++) {
      max = Math.max(max, memoryData.get(i));
    }
    return new Pair<>(Duration.between(start, end), max);
  }
}
```

We only added one new feature and our code is already growing too big. To make matters worse, it's still not scalable.

Let's break this apart. First we'll start with the data sources. For our sanity, we'll make a new type for the memory data:

```java
public final class MemorySnapshot {
  public final long reservedMemory;
  public final Instant timestamp;

  public MemorySnapshot(long reservedMemory, Instant timestamp) {
    this.reservedMemory = reservedMemory;
    this.timestamp = timestamp;
  }
}
Supplier<Instant> timeSource = Instant::now;
Supplier<MemorySnapshot> memorySource =
    () ->
        new MemorySnapshot(
            Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory(), Instant::now);
```

Next we write our data handling. We can separate most of what we wrote into another class and use type-checking to sort out the data:

```java
public final class DataProcessor {
  private final ArrayList<MemorySnapshot> memoryData = new ArrayList<>();

  private Instant start = Instant.MAX;
  private Instant end = Instant.EPOCH;

  public void add(Object o) {
    if (o instanceof MemorySnapshot) {
      memoryData.add((MemorySnapshot) o);
    } else if (o instanceof Instant) {
      Instant timestamp = (Instant) o;
      if (timestamp.compareTo(start) < 0) {
        start = timestamp;
      }
      if (timestamp.compareTo(end) > 0) {
        end = timestamp;
      }
    }
  }

  public Pair<Duration, MemorySnapshot> process() {
    long max = 0;
    for (int i = 0; i < memoryData.size(); i++) {
      max = Math.max(max, memoryData.get(i).reservedMemory);
    }
    return new Pair<>(Duration.between(start, end), max);
  }
}
```

The `DataProcessor` can just hold all the data and we can ask for it at our leisure.

Finally, we build the concurrent data collector. We can pull the code in `start` and `stop` right into a new class:

```java
public final class DataCollector {
  private final Executor executor = newSingleThreadScheduledExecutor();

  private boolean done = false;

  public void collect(Supplier<?> source, DataProcessor processor) {
    done = false;
    executor.execute(() -> {
      while (!done) {
        processor.add(source.get());
        try {
          Thread.sleep(1000);
        } catch (Exception e) {
          break;
        }
      }
    });
  }

  public void stop() {
    done = true;
  }
}
```

Now `collect` will do all the piping for us.

Let's rewrite the `Profiler`:

```java
public final class Profiler {
  private final List<Supplier<?>> syncSources = List.of(Instant::now);
  private final List<Supplier<?>> asyncSources = List.of(() -> new MemorySnapshot(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory(), Instant::now));

  private final DataProcessor processor = new DataProcessor();
  private final DataCollector collector = new DataCollector();

  public void start() {
    for (Supplier<?> source: syncSources) {
      processor.add(source.get());
    }
    for (Supplier<?> source: asyncSources) {
      collector.collect(source.get(), processor);
    }
  }

  public void stop() {
    for (Supplier<?> source: syncSources) {
      processor.add(source.get());
    }
    collector.stop();
  }

  public Pair<Duration, MemorySnapshot> read() {
    processor.process();
  }
}
```

Now we can add any number of data sources to our `Profiler` and update `DataProcessor` as needed. We can also test all the components in isolation, without relying on `Profiler` to bootstrap them. And we still have our helper method:

```java
public Duration profile(Runnable code) {
  Profiler profiler = new Profiler();
  profiler.start()
  code.run();
  profiler.stop()
  return profiler.read();
}
```
