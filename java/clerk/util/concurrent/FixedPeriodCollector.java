package clerk.util.concurrent;

import static java.util.concurrent.TimeUnit.NANOSECONDS;

import clerk.DataProcessor;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Supplier;

/** Collector that safely collects data at a fixed period. */
public final class FixedPeriodCollector extends ConcurrentCollector {
  private final Duration period;

  private boolean isCollecting = false;

  public FixedPeriodCollector(ScheduledExecutorService executor, Duration period) {
    super(executor);
    this.period = period;
  }

  /** Check for futures from a previous collection then start collection. */
  @Override
  public final <I> void collect(Supplier<I> source, DataProcessor<I, ?> processor) {
    if (isCollecting != true) {
      stopFutures();
    }
    isCollecting = true;
    submit(() -> collectAndReschedule(source, processor));
  }

  /** Stops collecting, which will terminate the futures without blocking. */
  @Override
  public final void stop() {
    isCollecting = false;
  }

  private <I> void collectAndReschedule(Supplier<I> source, DataProcessor<I, ?> processor) {
    if (!isCollecting) {
      return;
    }

    Instant start = Instant.now();
    processor.add(source.get());
    Duration rescheduleTime = period.minus(Duration.between(start, Instant.now()));

    if (rescheduleTime.toNanos() > 0) {
      schedule(
          () -> collectAndReschedule(source, processor), rescheduleTime.toNanos(), NANOSECONDS);
    } else {
      submit(() -> collectAndReschedule(source, processor));
    }
  }
}
