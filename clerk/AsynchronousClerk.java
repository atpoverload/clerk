package clerk;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.NANOSECONDS;

import clerk.util.ClerkLogger;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import java.util.logging.Logger;

/** A clerk that collects data asynchronously. */
public final class AsynchronousClerk<O> implements Clerk<O> {
  private static final Logger logger = ClerkLogger.getLogger();

  // data sources
  private final Iterable<Supplier<?>> sources;
  private final Processor<?, O> processor;

  // execution components
  // TODO(timurbey): replace this with some sort of policy
  private final Duration period;
  private final ScheduledExecutorService executor;

  // management so the tasks are terminated without shutting down the executor
  // TODO(timurbey): this should probably be a better locking mechanism, like a semaphore
  private final AtomicInteger tasks = new AtomicInteger(0);
  private final AtomicBoolean ready = new AtomicBoolean(true);

  private boolean isRunning = false;

  public AsynchronousClerk(
      Iterable<Supplier<?>> sources,
      Processor<?, O> processor,
      ScheduledExecutorService executor,
      Duration period) {
    this.sources = sources;
    this.processor = processor;
    this.executor = executor;
    this.period = period;
  }

  /**
   * Feeds the output of the data sources into the processor.
   *
   * <p>NOTE: the profiler will report a warning if this call is made while running.
   */
  @Override
  public void start() {
    if (!isRunning) {
      for (Supplier<?> source : sources) {
        executor.execute(
            () -> {
              while (!ready.get()) {}
              runAndReschedule(source);
              tasks.getAndIncrement();
            });
      }
      isRunning = true;
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
      ready.set(false);
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

  /**
   * Pipes data from a source into the processor and then reschedule it for the next period start.
   *
   * <p>If the executor has been told to stop, no new tasks are created. When the final task
   * terminates, the executor is reset to ready.
   */
  private void runAndReschedule(Supplier<?> source) {
    tasks.getAndDecrement();
    if (!ready.get()) {
      if (tasks.get() == 0) {
        ready.set(true);
      }
      return;
    }

    Instant start = Instant.now();
    Clerk.pipe(source, processor);
    Duration rescheduleTime = period.minus(Duration.between(start, Instant.now()));

    if (rescheduleTime.toMillis() > 0) {
      executor.schedule(() -> runAndReschedule(source), rescheduleTime.toMillis(), MILLISECONDS);
    } else if (rescheduleTime.toNanos() > 0) {
      executor.schedule(() -> runAndReschedule(source), rescheduleTime.toNanos(), NANOSECONDS);
    } else {
      executor.execute(() -> runAndReschedule(source));
    }

    tasks.getAndIncrement();
  }
}
