package clerk.examples;

import static java.util.concurrent.Executors.newSingleThreadScheduledExecutor;

import clerk.util.ListStorage;
import clerk.util.concurrent.FixedPeriodClerk;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;

public final class MemoryMonitor extends FixedPeriodClerk<List<Long>> {
  public MemoryMonitor(ScheduledExecutorService executor, Duration period) {
    super(
        () -> Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory(),
        new ListStorage<Long>(),
        executor,
        period);
  }

  private static double mean(List<Long> values) {
    double mean = 0;
    for (long value : values) {
      mean += value;
    }
    return mean / values.size();
  }

  public static void main(String[] args) throws Exception {
    ScheduledExecutorService executor = newSingleThreadScheduledExecutor();
    MemoryMonitor monitor = new MemoryMonitor(executor, Duration.ofMillis(1));

    monitor.start();
    Thread.sleep(1000);
    monitor.stop();
    System.out.println("avg bytes: " + mean(monitor.read()));

    executor.shutdown();
  }
}
