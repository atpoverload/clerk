package clerk.collectors;

import clerk.DataProcessor;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Supplier;

/** Collector that concurrently collects data as quickly as possible. */
public final class BurstCollector extends SchedulableCollector {
  private boolean isCollecting = false;

  public BurstCollector(ScheduledExecutorService executor) {
    super(executor);
  }

  /** Start collecting. */
  @Override
  public <I> void collect(Supplier<I> source, DataProcessor<I, ?> processor) {
    setCollectionState(true);
    schedule(() -> collectAndResubmit(source, processor));
  }

  /** Stop collecting. */
  @Override
  public void stop() {
    setCollectionState(false);
  }

  /** Run the workload and re-submit it immediately. */
  private <I> void collectAndResubmit(Supplier<I> source, DataProcessor<I, ?> processor) {
    if (!getCollectionState()) {
      return;
    }

    processor.add(source.get());
    schedule(() -> collectAndResubmit(source, processor));
  }
}
