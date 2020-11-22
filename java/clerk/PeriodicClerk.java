package clerk;

import static java.util.concurrent.Executors.newScheduledThreadPool;
import static java.util.concurrent.Executors.newSingleThreadScheduledExecutor;

import clerk.concurrent.PeriodicExecutor;
import clerk.util.ClerkUtil;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadFactory;
import java.util.function.Supplier;
import java.util.logging.Logger;

/** A clerk that collects data at a fixed period. */
public class PeriodicClerk<O> extends AbstractClerk<O> {
  private static final Logger logger = ClerkUtil.getLogger();
  private static final int DEFAULT_WORKER_COUNT = 4;
  private static final ThreadFactory daemonFactory =
      r -> {
        Thread t = new Thread(r);
        t.setDaemon(true);
        return t;
      };

  private final Executor executor;
  private final Duration period;

  public PeriodicClerk(Supplier<?> source, Processor<?, O> processor, Duration period) {
    super(List.of(source), processor);

    this.executor = new PeriodicExecutor(newSingleThreadScheduledExecutor(daemonFactory), period);
    this.period = period;
  }

  public PeriodicClerk(Iterable<Supplier<?>> sources, Processor<?, O> processor, Duration period) {
    super(sources, processor);

    int sourceCount = (int) sources.spliterator().getExactSizeIfKnown();
    int workers = sourceCount > 0 ? sourceCount : DEFAULT_WORKER_COUNT;
    this.executor = new PeriodicExecutor(newScheduledThreadPool(workers, daemonFactory), period);
    this.period = period;
  }

  /**
   * Feeds the output of the data sources into the processor.
   *
   * <p>NOTE: the profiler will report a warning if started while running.
   */
  @Override
  public final void start() {
    if (!isRunning) {
      isRunning = true;
      super.pipeData(executor);
    } else {
      logger.warning("clerk was told to start while running!");
    }
  }

  /**
   * Stops feeding data into the processor.
   *
   * <p>NOTE: the profiler will report a warning if stopped while not running.
   */
  @Override
  public final void stop() {
    if (isRunning) {
      isRunning = false;
    } else {
      logger.warning("clerk was told to stop while not running!");
    }
  }
}
