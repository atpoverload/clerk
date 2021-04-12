package clerk.examples;

import static java.util.concurrent.Executors.newSingleThreadScheduledExecutor;

import clerk.storage.ListStorage;
import clerk.util.BurstCollectionClerk;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;

public final class BurstStopwatch extends BurstCollectionClerk<Duration> {
  public BurstStopwatch(ScheduledExecutorService executor) {
    super(
        Instant::now,
        new ListStorage<Instant, Duration>() {
          @Override
          public Duration process() {
            List<Instant> data = getData();
            return Duration.between(data.get(0), data.get(data.size() - 1));
          }
        },
        executor);
  }

  public static void main(String[] args) throws Exception {
    ScheduledExecutorService executor = newSingleThreadScheduledExecutor();
    BurstStopwatch stopwatch = new BurstStopwatch(executor);

    stopwatch.start();
    Thread.sleep(1000);
    stopwatch.stop();
    System.out.println("slept for " + stopwatch.read());

    executor.shutdown();
  }
}
