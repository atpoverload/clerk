package clerk.examples;

import clerk.Clerk;
import clerk.concurrent.ClerkExecutorModule;
import clerk.examples.protos.ClerkExampleProtos.MemorySnapshot;
import clerk.util.ClerkUtil;
import dagger.Component;
import java.util.List;
import java.util.logging.Logger;

/**
 * A class that provides a clerk that returns {@link MemorySnapshot}s from the most recent session.
 *
 * <p>{@code ListStorage} clears the underlying data, so that the data can only be consumed from the
 * {@link Clerk} once.
 */
public class MemoryMonitor {
  @Component(modules = {ClerkExecutorModule.class, MemoryMonitorModule.class})
  interface ClerkFactory {
    Clerk<List<MemorySnapshot>> newClerk();
  }

  private static final ClerkFactory clerkFactory =
      DaggerMemoryMonitor_ClerkFactory.builder().build();

  public static Clerk<List<MemorySnapshot>> newMemoryMonitor() {
    return clerkFactory.newClerk();
  }

  private static final Logger logger = ClerkUtil.getLogger();

  private static List<MemorySnapshot> size(Runnable r) {
    Clerk<List<MemorySnapshot>> clerk = newMemoryMonitor();
    clerk.start();
    r.run();
    clerk.stop();
    return clerk.read();
  }

  private static long mean(Iterable<MemorySnapshot> snapshots) {
    long memory = 0;
    long size = 0;
    for (MemorySnapshot snapshot : snapshots) {
      memory += (snapshot.getTotalMemory() - snapshot.getFreeMemory());
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
    logger.info(iters + " loops, best of " + runs + ": " + totalMemory / 1000 + " per loop");
  }

  public static void main(String[] args) throws Exception {
    if (args.length == 0) {
      size(() -> {}, 10000000, 3);
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
          "\"" + String.join(" ", args) + "\" consumed " + mean(size(workload)) / 1000000 + " MB");
    }
  }
}
