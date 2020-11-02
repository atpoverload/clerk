package clerk.examples.inject;

import clerk.inject.Clerk;
import clerk.inject.ClerkExecutorModule;
import clerk.util.ClerkUtil;
import dagger.Component;
import java.util.List;
import java.util.logging.Logger;

/**
 * A class that provides a clerk that returns a {@link List<MemorySnapshot>} from {@code read()}
 * representing elapsed time since last call to {@code start()}.
 *
 * <p>If {@code read()} is called while running, the elapsed time since {@code start()} is returned.
 *
 * <p>If {@code read()} is called while not running, the elapsed time between {@code start()} and
 * {@code stop()} is returned.
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
    for (int i = 0; i < 5; i++) {
      clerk.start();
      r.run();
      clerk.stop();
      System.out.println(clerk.read());
    }
    return clerk.read();
  }

  private static long mean(Iterable<MemorySnapshot> snapshots) {
    long memory = 0;
    long size = 0;
    for (MemorySnapshot snapshot : snapshots) {
      memory += snapshot.memory;
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
      logger.info("ran \"" + String.join(" ", args) + "\" in " + size(workload));
    }
  }
}
