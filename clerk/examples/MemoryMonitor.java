package clerk.examples;

import static java.util.concurrent.Executors.newScheduledThreadPool;

import clerk.AsynchronousClerk;
import clerk.examples.data.ListStorage;
import clerk.scheduling.SteadyStateScheduler;
import clerk.util.ClerkLogger;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

/** A profiler that measures elapsed time between calls as a {@link Instant}. */
public class MemoryMonitor {
  private static final Logger logger = ClerkLogger.getLogger();

  private static final String DEFAULT_RATE_MS = "41";
  private static final Duration period =
      Duration.ofMillis(Long.parseLong(System.getProperty("clerk.sampling.rate", DEFAULT_RATE_MS)));

  private static final String DEFAULT_POOL_SIZE = "4";
  private static final AtomicInteger counter = new AtomicInteger();
  private static final ScheduledExecutorService executor =
      newScheduledThreadPool(
          Integer.parseInt(System.getProperty("clerk.sampling.workers", DEFAULT_POOL_SIZE)),
          r -> {
            Thread t = new Thread(r, clerkName());
            t.setDaemon(true);
            return t;
          });

  private static String clerkName() {
    return String.join("-", "clerk", String.format("%02d", counter.getAndIncrement()));
  }

  private static AsynchronousClerk<List<MemorySnapshot>> newMemoryMonitor() {
    return new AsynchronousClerk<>(
        List.of(
            () ->
                new MemorySnapshot(
                    Instant.now(),
                    Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())),
        new ListStorage<MemorySnapshot>(),
        executor,
        new SteadyStateScheduler(Instant::now, period));
  }

  private static List<MemorySnapshot> size(Runnable r) {
    AsynchronousClerk<List<MemorySnapshot>> clerk = newMemoryMonitor();
    clerk.start();
    r.run();
    clerk.stop();
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

  /** Amount of reserved memory at some instant. */
  public static class MemorySnapshot {
    public final Instant timestamp;
    public final long memory;

    public MemorySnapshot(Instant timestamp, long memory) {
      this.timestamp = timestamp;
      this.memory = memory;
    }

    @Override
    public String toString() {
      return String.join("=", timestamp.toString(), Long.toString(memory));
    }
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
