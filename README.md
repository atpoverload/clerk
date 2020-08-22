# `clerk`

`clerk` is a statistical profiling framework. Instead of providing a specific data target, `clerk` tries to unify common profiling strategies. Although `clerk` provides modules for users, it is intended as a skeleton to build from.

`clerk` is designed to be lightweight and precise. As a result, the framework can be a little strict. We do provide some basic units to eliminate boilerplate.

## building

If you want to use a pre-built profiler, you can pull the associated `jar`. `clerk` provides the following modules:

 - timer
 - memory-tracker
 - thread-tracker

If you want to build from source, you will need the following:

 - JDK 1.8 and 1.11
 - `bazel`
 <!-- - `maven` or `bazel` -->

To build a specific profiler, run `./build [profiling module]`.

## Profiling Applications

Integrating clerk into an application is (probably) the same as any other profiler:

```java
import clerk.Clerk;

public class A {
  private static Runnable getWorkload() {...}

  private static Runnable printSummary(T profile) {...}

  public static void main(String[] args) {
    Runnable workload = getWorkload();

    Clerk.start();
    workload.run();
    Clerk.stop();

    printSummary(Clerk.dump());
  }
}
```

`clerk.Clerk` provides the following interface:

```java
// starts the profiler. If a profiler is already running,
// the action is reported and ignored.
public static void start();

// stops the profiler. If a profiler is not running,
// the action is reported and ignored.
public static void stop();

// returns a profile from data collected since the last call of dump().
public static T dump();
```

## Developer API

`clerk` is intended for developer use, so it provides an API to help construct new profilers quickly.

There are a couple caveats in `clerk`'s design to allow it to maintain a lightweight structure:

 - `clerk` does not store processed data
 - `clerk` components are not dynamically constructed
 - `clerk` does not handle exceptions
 - `clerk` has no concept of race conditions

As a result, `clerk` requires some careful planning to be used effectively.

### clerk.core.Profiler

The base profiler exposes the same interface discussed in the profiling section. However, the underlying profiler is not inherently safe, so components can leak. As a result, it is a little safer to use a wrapper similar to `clerk.Clerk` that disposes and constructs the profiler as needed. In a future release, I hope to provide a method to glob modules together into a class, similar to `hilt`.

`clerk` uses a multiple-producer, single-consumer model to handle data sampling. Each profiler pipes the output of data sources into a processor using a scheduler. Users call `dump()` to retrieve data from the processor.

The profiler expects the following injections:

```java
Iterable<Supplier<I>> sources;
DataProcessor<I, O> processor;
Scheduler scheduler;
```

### Data Sources

`clerk` naively pipes the output of any data source into the processor. This means failures between the two components should be handled on the user side. This choice is made to prevent the need for debugging from within `clerk`.

`clerk` does not require data sources to be provided. This is helpful to build profilers that do not require sampling (such as `clerk.timer.TimerModule`).

### DataProcessor

Traditionally, profilers produce flat data traces to the user, requiring post-processing to make evaluations on data. While it is simple enough to dump flat profiles, one of the goals of `clerk` is simplify implementation of online processing. Successful implementations can support online optimizations, self-aware systems, and

`clerk` uses a simple data processing interface to consume data. This interface is somewhat flexible and only requires the developer to be consistent with their typing. It is quite simple to dump flat data as demonstrated in `clerk.api.ListStorage`. This interface also allows chaining of processors together to support modular operations. Please refer to chappie and eflect for examples of connecting processors to achieve this effect.

### Scheduler

`clerk` uses a scheduler to pipe data sources into processors:

```java
interface Scheduler {
  // runs a task
  public void schedule(Runnable r);

  // stops all running tasks
  public void cancel();
}
```

Although you can provide your own scheduler, it is recommend that you use one of `clerk`'s provided scheduling modules. They have been optimized for both performance and correctness.
