# `clerk`

`clerk` is a generalized data collection framework for `Java`. `clerk` simplifies construction of data pipelines by decomposing the procedure into sources and processing while providing structural tools to connect them easily. The framework can be used with vanilla Java or an injection framework.

## Building a data collector

We provide two kinds of clerks, a `clerk.SimpleClerk` and a injectable, periodic `clerk.inject.Clerk`. `SimpleClerk`s exist for completeness. In addition, building a `Stopwatch` is much easier.

## `SimpleClerk`s

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

Frequently, these collectors are used synchronously. This makes the injection framework overkill since the execution policies is easy to manage. This is done to explicitly differentiate between serial vs parallel data collection.

## `Clerk` with dependency injection

For parallel data collection, there is a injection framework that can enforce complex policies. This manages execution policies across multiple data source.
`Clerk` expects the following injections:

```java
@ClerkComponent Map<String, Supplier<?>> sources;
@ClerkComponent Map<String, Duration> periods;
Processor<?, O> processor;
@ClerkComponent ScheduledExecutorService executor;
```
