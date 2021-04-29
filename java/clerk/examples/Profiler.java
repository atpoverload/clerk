package clerk.examples;

import static java.util.concurrent.Executors.newSingleThreadScheduledExecutor;

import clerk.collectors.DirectCollector;
import clerk.collectors.FixedPeriodCollector;
import clerk.storage.ClassMappedListStorage;
import clerk.util.MappedClerk;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;

public final class Profiler extends MappedClerk<Map<Class<?>, List<Object>>> {
  public Profiler(ScheduledExecutorService executor, Duration period) {
    super(
        Map.of(
            () -> Instant.now(),
            new DirectCollector(),
            () -> Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory(),
            new FixedPeriodCollector(executor, period)),
        new ClassMappedListStorage<Object, Map<Class<?>, List<Object>>>() {
          @Override
          public Map<Class<?>, List<Object>> process() {
            return getData();
          }
        });
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
    Profiler monitor = new Profiler(executor, Duration.ofMillis(1));

    monitor.start();
    Thread.sleep(1000);
    monitor.stop();
    System.out.println(monitor.read());

    executor.shutdown();
  }
}
