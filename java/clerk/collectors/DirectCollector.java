package clerk.collectors;

import clerk.DataCollector;
import clerk.DataProcessor;
import java.util.ArrayList;
import java.util.function.Supplier;

/** Collector that collects data on the calling thread when starting or stopping the collector. */
public final class DirectCollector implements DataCollector {
  private final ArrayList<Runnable> collectors = new ArrayList<>();

  private boolean isRunning;

  /** Store the collector and run it. */
  @Override
  public <I> void collect(Supplier<I> source, DataProcessor<I, ?> processor) {
    synchronized (collectors) {
      if (!isRunning) {
        collectors.clear();
      }
      Runnable collector = () -> processor.add(source.get());
      collectors.add(collector);
      collector.run();
      isRunning = true;
    }
  }

  /** Run all stored collectors and discard them. */
  @Override
  public void stop() {
    synchronized (collectors) {
      if (isRunning) {
        for (Runnable collector : collectors) {
          collector.run();
        }
        collectors.clear();
        isRunning = false;
      }
    }
  }
}
