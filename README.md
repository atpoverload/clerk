# `clerk`

`clerk` is a lightweight data collection framework for `Java` runtimes with access to the [`Supplier`](https://docs.oracle.com/javase/8/docs/api/java/util/function/Supplier.html) interface. The API provides scalable out-of-the-box construction of data collectors while providing safety.

A vanilla `jar` can be built using `make`.

# Components

`clerk` provides three user API components:

```java
/** Interface that consumes raw data and produces processed data. */
public interface DataProcessor<I, O> {
  /** Adds data to the processor. */
  void add(I i);

  /** Returns processed data. */
  O process();
}
```

`DataProcessor`s deal with the data. `clerk.storage` provides synchronized `DataProcessor`s that behave similarly to [`ForwardingObject`](https://guava.dev/releases/19.0/api/docs/com/google/common/collect/ForwardingObject.html)s.

```java
/** Interface that connects a data source's output to a processor. */
public interface DataCollector {
  /** Starts collecting from a source. */
  <I> void collect(Supplier<I> source, DataProcessor<I, ?> processor);

  /** Stops all collection. */
  void stop();
}
```

`DataCollector`s connect sources to processors using some policy. `clerk.collectors` provide common collection strategies:

 - direct collection (start/stop)
 - fixed period collection
 - burst collection

```java
/** Interface for a data collection system. */
public interface Clerk<O> {
  /** Starts data collection. */
  void start();

  /** Stops data collection. */
  void stop();

  /** Returns data. */
  O read();
}
```

`Clerk`s are the user facing controls for collection. `clerk.util` provides a `SimpleClerk` that connects any number of `Supplier`s to a `DataProcessor` and a `DataCollector`, as well as wrappers for the provided `DataCollector`s.

`clerk.util` also provides a `MappedClerk`, which maps each source to a specific `DataCollector`. A `MappedClerk.Builder` is also available to avoid directly building the map.
