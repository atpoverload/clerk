package clerk.examples;

import static java.util.concurrent.Executors.newSingleThreadScheduledExecutor;

import clerk.FixedPeriodClerk;
import clerk.data.ReturnableListStorage;
import clerk.util.ClerkUtil;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import java.util.logging.Logger;

/**
 * A class that provides a clerk that returns {@link MemorySnapshot}s from the most recent session.
 *
 * <p>{@code ReturnableListStorage} clears the underlying data, so that the data can only be
 * consumed from the {@link Clerk} once.
 */
public final class MemoryMonitor extends FixedPeriodClerk<List<MemorySnapshot>> {
  private static final Logger logger = ClerkUtil.getLogger();
  private static final AtomicInteger counter = new AtomicInteger();
  private static final ThreadFactory daemonThreadFactory =
      r -> {
        Thread t = new Thread(r, "clerk-" + counter.getAndIncrement());
        t.setDaemon(true);
        return t;
      };
  private static final Supplier<MemorySnapshot> snapshotSource =
      () ->
          new MemorySnapshot(
              Instant.now(), Runtime.getRuntime().totalMemory(), Runtime.getRuntime().freeMemory());

  public MemoryMonitor(ScheduledExecutorService executor) {
    super(
        snapshotSource,
        new ReturnableListStorage<MemorySnapshot>(),
        executor,
        Duration.ofMillis(4));
  }

  /** Size a runnable. */
  public static void size(Runnable r, int iters, int runs) {
    ScheduledExecutorService executor = newSingleThreadScheduledExecutor(daemonThreadFactory);
    MemoryMonitor monitor = new MemoryMonitor(executor);
    long best = Long.MAX_VALUE;
    for (int i = 0; i < runs; i++) {
      long memory = 0;
      monitor.start();
      for (int j = 0; j < iters; j++) {
        r.run();
      }
      monitor.stop();
      memory += mean(monitor.read());
      if (memory < best) {
        best = memory;
      }
    }
    executor.shutdown();
    logger.info(
        iters
            + " loops, best of "
            + runs
            + ": "
            + String.format("%.2f", (double) best / iters / 1000)
            + "MB per loop");
  }

  private static long mean(Iterable<MemorySnapshot> snapshots) {
    long memory = 0;
    long size = 0;
    for (MemorySnapshot snapshot : snapshots) {
      memory += snapshot.totalMemory - snapshot.freeMemory;
      size++;
    }
    if (size > 0) {
      return memory / size;
    } else {
      return Long.MAX_VALUE;
    }
  }

  public static void main(String[] args) throws Exception {
    int n = 10000;
    Runnable r =
        () -> {
          ArrayList<Integer> l = new ArrayList<>(n);
          for (int i = 0; i < n; i++) {
            l.add(i);
          }
        };
    size(r, 10000, 3);
  }
}
