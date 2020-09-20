# `clerk`

`clerk` is a generalized data collection framework. Instead of providing a specific data target, `clerk` tries to unify common strategies using [dagger](https://dagger.dev/) while maintaining a precise, lightweight, and easily configurable back-end.

## Building a profiler

`clerk` is implemented under the assumption that a user intends to collect and process data from a source. A decoupled implementation may look like:

<!-- change the code below to actually profiler correctly -->
```java
Supplier<Foo> source = new FooDataSource();
ArrayList<Foo> data = new ArrayList<>();
for (int i = 0; i < 100; i++) {
  data.add(source.get());
}

Function<ArrayList<Foo>, Bar> processor = new FoosToBarProcessor();
Bar result = processor.apply(data);
```

It is simple enough to wrap this into a class:

```java
public final class Profiler {
  public Profiler(source, data, processor);

  public Bar profile() {
    for (int i = 0; i < 100; i++) {
      data.add(source.get());
    }
    Bar result = processor.apply(data);
    data.clear();
    return result;
  }
}
```

This is not extensible; even attempting to change the return type requires changes to both the processor and the profiler. While this is trivial for individual projects, consider large scale systems, like a benchmarking framework. It would be nice to extend our profiler's behavior without re-writing code'.

`clerk` attempts to address this problem in Java by decoupling the data collection into data sources, processors, and execution.

 - `DataSource`: `Iterable<Supplier<?>>` for anonymous sampling.
 - `Processor`: extension of `Consumer` and `Supplier` interfaces to consume data and process data.
 - `Execution`: an `Executor` that ties a user API to the data collection.

Once these components have been designed, `clerk` users dagger to assemble an output-typed `Profiler`. As a result, there is some strictness regarding component design to ensure they do not break. With this model, a `clerk` implementation could be:

```java
public final class Profiler {
  class FoosToBarProcessor implements Processor<Foo, Bar> {
    private final ArrayList<Foo> data = new ArrayList<>();

    @Override
    public void accept(Foo foo) {
      data.add(foo);
    }

    @Override
    public Bar get() {
      return foosToBar(data);
    }
  }
  @Module
  interface FooBarModule {
    @Provides
    @DataSource
    @IntoSet
    static Supplier<?> provideSource() {
      return Foo::new
    }

    @Provides
    static Processor<?, Bar> provideProcessor() {
      return new FoosToBarProcessor();
    }
  }

  @Component(modules = {FooBarModule.class, PeriodicSamplingModule.class})
  interface ClerkFactory {
    Clerk<Bar> newClerk();
  }

  private static final ClerkFactory clerkFactory = DaggerProfiler_ClerkFactory.builder().build();

  public static Clerk newProfiler() {
    return clerkFactory.newClerk();
  }
}
```

Here are some guidelines to a reliable profiler:

### Keep data sources lightweight

`clerk` makes no assumptions about the user's code when connecting data sources. Instead, it allows an execution module to generalize connections. This allows decoupling of data collection into processing stages. Instead of implementing all code within the data source, instead let `clerk` send it downstream to the appropriate consumer. We provide some common execution strategies but you may need to fine-tune it to your environment.

### Enforce safety during transactions

`clerk` will pull the result of the provided processor when requested to `stop` or `dump`. With the asynchronous modules, this can create race conditions in shared data structures. Some of `clerk`'s modules can provide limited protection against concurrent races but it cannot enforce type safety. Although this will not necessarily cause your program to crash, it will likely prevent data collection.
