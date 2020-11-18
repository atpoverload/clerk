package clerk.clerks;

import static java.util.concurrent.Executors.newScheduledThreadPool;
import static java.util.concurrent.Executors.newSingleThreadScheduledExecutor;

import clerk.Processor;
import clerk.util.ClerkUtil;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Supplier;
import java.util.logging.Logger;

/** A clerk that periodically collects data at a steady state interval. */
public class PeriodicClerk<O> {
  private static final Logger logger = ClerkUtil.getLogger();

  private final Iterable<Supplier<?>> sources;
  private final Processor<?, O> processor;
  private final Duration period;
  private final ScheduledExecutorService executor;

  private boolean isRunning = false;

  public PeriodicClerk(Supplier<?> source, Processor<?, O> processor, Duration period) {
    this.sources = List.of(source);
    this.processor = processor;
    this.period = period;
    this.executor =
        newSingleThreadScheduledExecutor(
            r -> {
              Thread t = new Thread(r);
              t.setDaemon(true);
              return t;
            });
  }

  public PeriodicClerk(Iterable<Supplier<?>> sources, Processor<?, O> processor, Duration period) {
    this.sources = sources;
    this.processor = processor;
    this.period = period;

    int workers = 4;
    int sourceCount = (int) sources.spliterator().getExactSizeIfKnown();
    System.out.println(workers);
    System.out.println(sourceCount);
    if (sourceCount > 0) {
      workers = sourceCount;
    }
    this.executor =
        newScheduledThreadPool(
            workers,
            r -> {
              Thread t = new Thread(r);
              t.setDaemon(true);
              return t;
            });
  }

  /**
   * Feeds the output of the data sources into the processor.
   *
   * <p>NOTE: the profiler will report a warning if started while running.
   */
  public final void start() {
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
   * <p>NOTE: the profiler will report a warning if stopped while not running.
   */
  public final void stop() {
    if (isRunning) {
      isRunning = false;
    } else {
      logger.warning("clerk was told to stop while not running!");
    }
  }

  /**
   * Puts a sample from each data source into the processor if the clerk is running, and then
   * returns the result of the process.
   */
  public final O read() {
    if (isRunning) {
      pipeData();
    }
    return processor.process();
  }

  private void pipeData() {
    for (Supplier<?> source : sources) {
      executor.execute(
          () -> {
            if (!isRunning) {
              throw new RuntimeException("the clerk was terminated");
            }
            ClerkUtil.pipe(source, processor);
          });
    }
  }
}
