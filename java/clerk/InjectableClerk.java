package clerk;

import clerk.util.ClerkUtil;
import java.util.Map;
import java.util.function.Supplier;
import java.util.logging.Logger;
import javax.inject.Inject;

/** A clerk that can be built with an injection framework. */
// TODO(timurbey): since this is where most of the magic happens, let's write more docs.
public final class InjectableClerk<O> implements Clerk<O> {
  public static final String DEFAULT_POLICY_KEY = "default_policy";
  private static final Logger logger = ClerkUtil.getLogger();

  private final Map<String, Supplier<?>> sources;
  private final Processor<?, O> processor;
  private final Map<String, CollectionPolicy> policies;

  private boolean isRunning = false;

  @Inject
  InjectableClerk(
      @ClerkComponent Map<String, Supplier<?>> sources,
      @ClerkComponent Processor<?, O> processor,
      @ClerkComponent Map<String, CollectionPolicy> policies) {
    this.sources = sources;
    this.processor = processor;
    this.policies = policies;
  }

  /**
   * Pipes data into the processor.
   *
   * <p>NOTE: the profiler will report a warning if this call is made while running.
   */
  @Override
  public void start() {
    if (!isRunning) {
      isRunning = true;
      startCollection();
    } else {
      logger.warning("clerk was told to start while running!");
    }
  }

  /**
   * Stops piping data into the processor.
   *
   * <p>NOTE: the profiler will report a warning if this call is made while not running.
   */
  @Override
  public void stop() {
    if (isRunning) {
      isRunning = false;
      stopCollection();
    } else {
      logger.warning("clerk was told to stop while not running!");
    }
  }

  /** Return the processor's output. */
  @Override
  public O read() {
    return processor.process();
  }

  private void startCollection() {
    for (String sourceName : sources.keySet()) {
      // guard against an injection for the default policy, which should have no source
      if (sourceName == DEFAULT_POLICY_KEY) {
        continue;
      }
      policies
          .getOrDefault(sourceName, policies.get(DEFAULT_POLICY_KEY))
          .start(sources.get(sourceName), processor);
    }
  }

  private void stopCollection() {
    for (String sourceName : sources.keySet()) {
      policies.getOrDefault(sourceName, policies.get(DEFAULT_POLICY_KEY)).stop();
    }
  }
}
