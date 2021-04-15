package clerk.collectors;

import static java.util.concurrent.TimeUnit.NANOSECONDS;

import clerk.DataProcessor;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Supplier;

/** Collector that concurrently collects data at a fixed period. */
public final class FixedPeriodCollector extends SchedulableCollector {
  private final Duration period;

  public FixedPeriodCollector(ScheduledExecutorService executor, Duration period) {
    super(executor);
    this.period = period;
  }

  /** Start collecting. */
  @Override
  public <I> void collect(Supplier<I> source, DataProcessor<I, ?> processor) {
    setCollectionState(true);
    schedule(() -> collectAndReschedule(source, processor));
  }

  /** Stop collecting. */
  @Override
  public void stop() {
    setCollectionState(false);
  }

  /** Run the workload and re-schedule it for the next period start. */
  private <I> void collectAndReschedule(Supplier<I> source, DataProcessor<I, ?> processor) {
    if (!getCollectionState()) {
      return;
    }

    Instant start = Instant.now();
    processor.add(source.get());
    Duration rescheduleTime = period.minus(Duration.between(start, Instant.now()));

    if (rescheduleTime.toNanos() > 0) {
      schedule(
          () -> collectAndReschedule(source, processor), rescheduleTime.toNanos(), NANOSECONDS);
    } else {
      schedule(() -> collectAndReschedule(source, processor));
    }
  }
}
