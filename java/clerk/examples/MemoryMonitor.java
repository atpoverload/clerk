package clerk.examples;

import static java.util.concurrent.Executors.newSingleThreadScheduledExecutor;

import clerk.util.SimpleAggregator;
import clerk.util.concurrent.FixedPeriodClerk;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;

public final class MemoryMonitor extends FixedPeriodClerk<Long> {
  public MemoryMonitor(ScheduledExecutorService executor, Duration period) {
    super(
        () -> Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory(),
        new SimpleAggregator<Long, Long>() {
          @Override
          public Long aggregate(List<Long> data) {
            return mean(data);
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
    System.out.println("total bytes: " + monitor.read() + "B");

    executor.shutdown();
  }
}
