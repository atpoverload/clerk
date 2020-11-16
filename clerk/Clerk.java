package clerk;

import clerk.execution.ExecutionPolicy;
import clerk.util.ClerkUtil;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Supplier;
import java.util.logging.Logger;
import javax.inject.Inject;

/** An asynchronous clerk that is built with dagger. */
// TODO(timurbey): since this is where most of the magic happens, let's write more docs.
public final class Clerk<O> {
  public static final String DEFAULT_POLICY_KEY = "default_policy";

  private static final Logger logger = ClerkUtil.getLogger();

  private final Map<String, Supplier<?>> sources;
  private final Processor<?, O> processor;
  private final Map<String, ExecutionPolicy> policies;
  private final ScheduledExecutorService executor;

  private boolean isRunning = false;

  @Inject
  public Clerk(
      @ClerkComponent Map<String, Supplier<?>> sources,
      Processor<?, O> processor,
      @ClerkComponent Map<String, ExecutionPolicy> policies,
      @ClerkComponent ScheduledExecutorService executor) {
    this.sources = sources;
    this.processor = processor;
    this.policies = policies;
    this.executor = executor;
  }

  /**
   * Feeds the output of the data sources into the processor.
   *
   * <p>NOTE: the profiler will report a warning if this call is made while running.
   */
  public void start() {
    if (!isRunning) {
      isRunning = true;
      for (String sourceName : sources.keySet()) {
        executor.execute(
            () -> {
              policies
                  .getOrDefault(sourceName, policies.get(DEFAULT_POLICY_KEY))
                  .execute(() -> ClerkUtil.pipe(sources.get(sourceName), processor), executor);
            });
      }
    } else {
      logger.warning("clerk was told to start while running!");
    }
  }

  /**
   * Stops feeding data into the processor.
   *
   * <p>NOTE: the profiler will report a warning if this call is made while not running.
   */
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
  public O read() {
    return processor.process();
  }
}
