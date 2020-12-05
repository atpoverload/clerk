package clerk;

import static clerk.util.ClerkUtil.daemonThreadFactory;
import static java.util.concurrent.Executors.newScheduledThreadPool;
import static java.util.concurrent.Executors.newSingleThreadScheduledExecutor;

import clerk.data.FixedPeriodPolicy;
import clerk.util.ClerkUtil;
import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Supplier;
import java.util.logging.Logger;

/** A clerk that collects data at a fixed period. */
// TODO(timurbey): there's a potential race if start() gets called concurrently.
public class FixedPeriodClerk<O> implements Clerk<O> {
  private static final Logger logger = ClerkUtil.getLogger();

  private final Iterable<Supplier<?>> sources;
  private final Processor<?, O> processor;
  private final ScheduledExecutorService executor;
  private final FixedPeriodPolicy policy;

  private boolean isRunning = false;

  public FixedPeriodClerk(Supplier<?> source, Processor<?, O> processor, Duration period) {
    this.sources = List.of(source);
    this.processor = processor;
    this.executor = newSingleThreadScheduledExecutor(daemonThreadFactory());
    this.policy = new FixedPeriodPolicy(this.executor, period);
  }

  public FixedPeriodClerk(
      Collection<Supplier<?>> sources, Processor<?, O> processor, Duration period) {
    this.sources = sources;
    this.processor = processor;
    // try to make a thread for each source
    // TODO(timurbey): should there be a limit?
    int workers = sources.size();
    this.executor = newScheduledThreadPool(workers, daemonThreadFactory());
    this.policy = new FixedPeriodPolicy(this.executor, period);
  }

  /**
   * Pipes data into the processor.
   *
   * <p>NOTE: the profiler will report a warning if started while running.
   */
  @Override
  public final void start() {
    if (!isRunning) {
      startCollecting();
      isRunning = true;
    } else {
      logger.warning("clerk was told to start while running!");
    }
  }

  /**
   * Stops piping data into the processor.
   *
   * <p>NOTE: the profiler will report a warning if stopped while not running.
   */
  @Override
  public final void stop() {
    if (isRunning) {
      policy.stop();
      isRunning = false;
    } else {
      logger.warning("clerk was told to stop while stopped!");
    }
  }

  /** Return the processor's output. */
  @Override
  public final O read() {
    return processor.process();
  }

  /** Shutdown the executor so the clerk cannot be reused. */
  public final void terminate() {
    executor.shutdown();
  }

  private void startCollecting() {
    for (Supplier<?> source : sources) {
      policy.start(source, processor);
    }
  }
}
