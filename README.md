# `clerk`

`clerk` is a generalized data collection framework for `Java`. `clerk` simplifies construction of data pipelines by decomposing the procedure into sources and processing while providing structural tools to connect them. The framework can be used with vanilla `Java` or an injection framework.

# Building

A vanilla `clerk` release can be built with `ant jar`. The `clerk.jar` contains all non-injection code and the contents of `examples/vanilla`.

We provide two kinds of clerks:
 - the `SimpleClerk`
 - the injection backed `Clerk`

`SimpleClerk` is better when the data is well known.
link to the simple clerk package
link to examples where the simple clerk is better


## `clerk.Clerk`

`Clerk` is an injection framework that decouples data collection into three pieces - data sources, data processing, and collection policies. While this isn't necessary for all data collection problems, frequently data collectors do not have identical policies. Consider the case of an Android device; we have the ability to both periodically sample and set up handlers for events in the Android OS. When collecting this data, a general manager is preferable because it will be easier to synchronize.

Typically, data collection implementations need to reuse components. Sharing these components correctly is critical for performance. Most applications use `java.util.concurrent` to manage data collection asynchronously.

`Clerk` expects the following injections:

```java
@ClerkComponent Map<String, Supplier<?>> sources;
@ClerkComponent Map<String, Executor> policies;
Processor<?, O> processor;
```

### Sources
