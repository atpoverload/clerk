# `clerk`

`clerk` is a lightweight data collection, handling, and processing framework for `Java` runtimes with access to the [`Supplier`](https://docs.oracle.com/javase/8/docs/api/java/util/function/Supplier.html) interface. The API provides scalable out-of-the-box construction of data collectors while providing data and thread safety. This helps the user focus on the data itself instead of infrastructure.

A vanilla `clerk.jar` can be built using `make`.

# Components

`clerk` provides three user API components:

---

```java
/** Interface that consumes raw data and produces processed data. */
public interface DataProcessor<I, O> {
  /** Adds data to the processor. */
  void add(I i);

  /** Returns processed data. */
  O process();
}
```

`DataProcessors` handle data, which usually includes both storage and processing. `clerk.storage` provides synchronized `DataProcessors` that behave similarly to [`ForwardingObjects`](https://guava.dev/releases/19.0/api/docs/com/google/common/collect/ForwardingObject.html), with a `getData` method to safely retrieve the current data.

---

```java
/** Interface that connects a data source's output to a processor. */
public interface DataCollector {
  /** Starts collecting from a source. */
  <I> void collect(Supplier<I> source, DataProcessor<I, ?> processor);

  /** Stops all collection. */
  void stop();
}
```

`DataCollectors` connect sources to processors using a policy. `clerk.collectors` provide some common collection strategies:

 - direct collection (start/stop)
 - fixed period collection
 - burst collection

---

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

`Clerks` are the user facing controls for collection. `clerk.util` provides a `SimpleClerk` that connects any number of `Suppliers` to a `DataProcessor` and a `DataCollector`, as well as wrappers for the provided `DataCollectors`.

`clerk.util` also provides a `MappedClerk`, which maps each source to a specific `DataCollector`. A `MappedClerk.Builder` is also available to avoid directly building the map.
