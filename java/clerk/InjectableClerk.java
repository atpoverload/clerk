package clerk;

import clerk.util.ClerkUtil;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.function.Supplier;
import java.util.logging.Logger;
import javax.inject.Inject;

/** An asynchronous clerk that is built with dagger. */
// TODO(timurbey): since this is where most of the magic happens, let's write more docs.
public final class InjectableClerk<O> implements Clerk<O> {
  public static final String DEFAULT_POLICY_KEY = "default_policy";

  private static final Logger logger = ClerkUtil.getLogger();

  private final Map<String, Supplier<?>> sources;
  private final Processor<?, O> processor;
  private final Map<String, Executor> policies;

  private boolean isRunning = false;

  @Inject
  InjectableClerk(
      @ClerkComponent Map<String, Supplier<?>> sources,
      Processor<?, O> processor,
      @ClerkComponent Map<String, Executor> policies) {
    this.sources = sources;
    this.processor = processor;
    this.policies = policies;
  }

  /**
   * Feeds the output of the data sources into the processor.
   *
   * <p>NOTE: the profiler will report a warning if this call is made while running.
   */
  @Override
  public void start() {
    if (!isRunning) {
      isRunning = true;
      pipeData();
    } else {
      logger.warning("clerk was told to start while running!");
    }
  }

  /**
   * Stops feeding data into the processor.
   *
   * <p>NOTE: the profiler will report a warning if this call is made while not running.
   */
  @Override
  public void stop() {
    if (isRunning) {
      isRunning = false;
    } else {
      logger.warning("clerk was told to stop while not running!");
    }
  }

  /** Returns the output of the processor. */
  // TODO(timurbey): it may be better to enforce the return of a listenable future with the
  // generics. we would have to deal with assembling the futures ourselves
  @Override
  public O read() {
    return processor.process();
  }

  /** Starts async collection of data from all sources. */
  private void pipeData() {
    for (String sourceName : sources.keySet()) {
      policies
          .getOrDefault(sourceName, policies.get(DEFAULT_POLICY_KEY))
          .execute(
              () -> {
                if (!isRunning) {
                  throw new RuntimeException("the clerk was terminated");
                }
                try {
                  Clerk.pipe(sources.get(sourceName), processor);
                } catch (ClassCastException e) {
                  logger.severe(
                      "data source "
                          + sources.get(sourceName).getClass()
                          + " did not produce the expected type:");
                  logger.severe(e.getMessage().split("\\(")[0]);
                  throw e;
                }
              });
    }
  }
}
