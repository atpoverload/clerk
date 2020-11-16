# `SimpleClerk`s

We provide two `SimpleClerk` implementations you can immediately use, the `DirectClerk` and the `DeferredClerk`. Both are implemented with the following expectations:

 - The clerk goes into the `RUNNING` state and collects data once from all data sources when `start()` is called.

 - The clerk goes into the `NOT RUNNING` and collects data once from all data sources state when `stop()` is called if the clerk is `RUNNING`.

 - The clerk collects data once from all data sources when `read()` is called if the clerk is `RUNNING`. Then it returns the output of the processor.

These clerks implement straightforward data collection policies for fast prototyping. Typically, you want to assemble a basic data collector:
```java
public class Stopwatch extends DirectClerk<Duration> {
  public Stopwatch() {
    super(
        () -> Instant.now(),
        new PairStorage<Instant, Duration>() {
          @Override
          public Duration process() {
            return Duration.between(getFirst(), getSecond());
          }
        });
  }
}

Stopwatch stopwatch = new Stopwatch();

stopwatch.start();
workload.run();
stopwatch.stop();

System.out.println("ran workload in " + stopwatch.read());
```

Frequently, these collectors are used synchronously. This makes the injection framework overkill since the execution policy is easy to manage. This is done to explicitly differentiate between serial vs parallel data collection.
