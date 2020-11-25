package clerk;

import static java.util.concurrent.Executors.newScheduledThreadPool;
import static java.util.concurrent.Executors.newSingleThreadScheduledExecutor;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.NANOSECONDS;

import clerk.util.ClerkUtil;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.function.Supplier;
import java.util.logging.Logger;

/** A clerk that collects data at a fixed period. */
// TODO(timurbey): there's a potential race if start() gets called concurrently.
public class FixedPeriodClerk<O> implements Clerk<O> {
  private static final Logger logger = ClerkUtil.getLogger();
  private static final int DEFAULT_WORKER_COUNT = 4;
  private static final ThreadFactory daemonFactory =
      r -> {
        Thread t = new Thread(r);
        t.setDaemon(true);
        return t;
      };

  private final Iterable<Supplier<?>> sources;
  private final Processor<?, O> processor;
  private final Duration period;
  private final ScheduledExecutorService executor;
  private final ArrayList<Future<?>> dataFutures = new ArrayList<>();

  private boolean isRunning = false;

  public FixedPeriodClerk(Supplier<?> source, Processor<?, O> processor, Duration period) {
    this.sources = List.of(source);
    this.processor = processor;
    this.period = period;
    this.executor = newSingleThreadScheduledExecutor(daemonFactory);
  }

  public FixedPeriodClerk(
      Iterable<Supplier<?>> sources, Processor<?, O> processor, Duration period) {
    this.sources = sources;
    this.processor = processor;
    this.period = period;
    // try to make a thread for each source
    // TODO(timurbey): should there be a limit?
    int sourceCount = (int) sources.spliterator().getExactSizeIfKnown();
    int workers = sourceCount > 0 ? sourceCount : DEFAULT_WORKER_COUNT;
    this.executor = newScheduledThreadPool(workers, daemonFactory);
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
    // make sure the previous futures are done or cancelled
    for (Future<?> future : dataFutures) {
      // attempt to cancel the future; if we can't, get the result safely
      if (!future.isDone() && !future.isCancelled() && !future.cancel(false)) {
        try {
          future.get();
        } catch (Exception e) {
          logger.warning("could not consume a data future");
          e.printStackTrace();
        }
      }
    }
    dataFutures.clear();

    isRunning = true;
    for (Supplier<?> source : sources) {
      dataFutures.add(executor.submit(() -> runAndReschedule(() -> Clerk.pipe(source, processor))));
    }
  }

  private void runAndReschedule(Runnable r) {
    if (!isRunning) {
      return;
    }

    Instant start = Instant.now();
    r.run();
    Duration rescheduleTime = period.minus(Duration.between(start, Instant.now()));

    if (rescheduleTime.toMillis() > 0) {
      dataFutures.add(
          executor.schedule(() -> runAndReschedule(r), rescheduleTime.toMillis(), MILLISECONDS));
    } else if (rescheduleTime.toNanos() > 0) {
      dataFutures.add(
          executor.schedule(() -> runAndReschedule(r), rescheduleTime.toNanos(), NANOSECONDS));
    } else {
      dataFutures.add(executor.submit(() -> runAndReschedule(r)));
    }
  }
}
