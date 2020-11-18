package clerk.examples;

import clerk.PeriodicClerk;
import clerk.data.ListStorage;
import clerk.util.ClerkUtil;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.logging.Logger;

/**
 * A class that provides a clerk that returns {@link MemorySnapshot}s from the most recent session.
 *
 * <p>{@code ListStorage} clears the underlying data, so that the data can only be consumed from the
 * {@link Clerk} once.
 */
public final class MemoryMonitor extends PeriodicClerk<List<MemorySnapshot>> {
  private static final Logger logger = ClerkUtil.getLogger();

  private static final Supplier<MemorySnapshot> snapshotSource =
      () ->
          new MemorySnapshot(
              Instant.now(), Runtime.getRuntime().totalMemory(), Runtime.getRuntime().freeMemory());

  public MemoryMonitor() {
    super(snapshotSource, new ListStorage<MemorySnapshot>(), Duration.ofMillis(4));
  }

  private static List<MemorySnapshot> size(Runnable r) {
    MemoryMonitor clerk = new MemoryMonitor();
    clerk.start();
    r.run();
    clerk.stop();
    return clerk.read();
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

  /** Times a workload similar to python's timeit module. */
  public static void size(Runnable workload, int iters, int runs) {
    long totalMemory = Long.MAX_VALUE;
    for (int i = 0; i < runs; i++) {
      long memory =
          mean(
              size(
                  () -> {
                    for (int j = 0; j < iters; j++) {
                      workload.run();
                    }
                  }));
      if (memory < totalMemory) {
        totalMemory = memory;
      }
    }
    logger.info(iters + " loops, best of " + runs + ": " + totalMemory / 1000000 + "MB per loop");
  }

  public static void main(String[] args) throws Exception {
    if (args.length == 0) {
      int n = 100000;
      size(
          () -> {
            ArrayList<Integer> l = new ArrayList<>();
            for (int i = 0; i < n; i++) {
              l.add(i);
            }
          },
          10000,
          3);
    } else if (args.length > 0) {
      Runnable workload =
          () -> {
            try {
              new ProcessBuilder(args).start().waitFor();
            } catch (Exception e) {
              logger.info("unable to run command \"" + String.join(" ", args) + "\"");
              e.printStackTrace();
            }
          };
      logger.info(
          "\"" + String.join(" ", args) + "\" consumed " + mean(size(workload)) / 1000000 + "MB");
    }
  }
}
