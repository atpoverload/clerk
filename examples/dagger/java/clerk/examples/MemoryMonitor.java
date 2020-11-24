package clerk.examples;

import clerk.Clerk;
import clerk.InjectableClerk;
import clerk.dagger.ClerkExecutionModule;
import clerk.util.ClerkUtil;
import dagger.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * A class that provides a clerk that returns {@link MemorySnapshot}s from the most recent session.
 *
 * <p>{@code ListStorage} clears the underlying data, so that the data can only be consumed from the
 * {@link Clerk} once.
 */
public class MemoryMonitor {
  @Component(modules = {ClerkExecutionModule.class, MemoryMonitorModule.class})
  interface ClerkFactory {
    InjectableClerk<List<MemorySnapshot>> newClerk();
  }

  private static final ClerkFactory clerkFactory =
      DaggerMemoryMonitor_ClerkFactory.builder().build();

  public static InjectableClerk<List<MemorySnapshot>> newMemoryMonitor() {
    return clerkFactory.newClerk();
  }

  private static final Logger logger = ClerkUtil.getLogger();

  /** Size a runnable. */
  public static void size(Runnable r, int iters, int runs) {
    InjectableClerk<List<MemorySnapshot>> monitor = newMemoryMonitor();
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
