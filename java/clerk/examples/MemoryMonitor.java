package clerk.examples;

import static java.util.concurrent.Executors.newSingleThreadScheduledExecutor;

import clerk.storage.ListStorage;
import clerk.util.FixedPeriodClerk;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;

public final class MemoryMonitor extends FixedPeriodClerk<List<Long>> {
  public MemoryMonitor(ScheduledExecutorService executor, Duration period) {
    super(
        () -> Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory(),
        new ListStorage<Long, List<Long>>() {
          @Override
          public List<Long> process() {
            return getData();
          }
        },
        executor,
        period);
  }

  private static long mean(List<Long> values) {
    long mean = 0;
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
    System.out.println("total bytes: " + mean(monitor.read()) + "B");

    executor.shutdown();
  }
}
