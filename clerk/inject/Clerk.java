package clerk.inject;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.NANOSECONDS;

import clerk.Processor;
import clerk.util.ClerkUtil;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Supplier;
import java.util.logging.Logger;
import javax.inject.Inject;

/** An asynchronous clerk that is built with dagger. */
public final class Clerk<O> {
  private static final Logger logger = ClerkUtil.getLogger();

  private final Map<String, Supplier<?>> sources;
  private final Processor<?, O> processor;
  private final Map<String, Duration> periods;
  private final ScheduledExecutorService executor;

  private boolean isRunning = false;

  @Inject
  public Clerk(
      @ClerkComponent Map<String, Supplier<?>> sources,
      Processor<?, O> processor,
      @ClerkComponent Map<String, Duration> periods,
      @ClerkComponent ScheduledExecutorService executor) {
    this.sources = sources;
    this.processor = processor;
    this.periods = periods;
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
      for (String source : sources.keySet()) {
        executor.execute(
            () -> {
              runAndReschedule(source);
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

  /**
   * Pipes data from a source into the processor and then reschedule it for the next period start.
   *
   * <p>If the executor has been told to stop, no new tasks are created. When the final task
   * terminates, the executor is reset to ready.
   */
  private void runAndReschedule(String source) {
    if (!isRunning) {
      return;
    }

    Instant start = Instant.now();
    ClerkUtil.pipe(sources.get(source), processor);
    Duration rescheduleTime =
        periods
            .getOrDefault(source, periods.get("default_period"))
            .minus(Duration.between(start, Instant.now()));

    if (rescheduleTime.toMillis() > 0) {
      executor.schedule(() -> runAndReschedule(source), rescheduleTime.toMillis(), MILLISECONDS);
    } else if (rescheduleTime.toNanos() > 0) {
      executor.schedule(() -> runAndReschedule(source), rescheduleTime.toNanos(), NANOSECONDS);
    } else {
      executor.execute(() -> runAndReschedule(source));
    }
  }
}
