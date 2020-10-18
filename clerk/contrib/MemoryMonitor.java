package clerk.contrib;

import static java.util.concurrent.Executors.newScheduledThreadPool;

import clerk.Clerk;
import clerk.Processor;
import clerk.data.AsynchronousSteadyStateExecutor;
import clerk.util.ClerkLogger;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

/** A profiler that measures elapsed time between calls as a {@link Instant}. */
public class MemoryMonitor {
  private static final Logger logger = ClerkLogger.getLogger();
  private static final String DEFAULT_RATE_MS = "41";
  private static final String DEFAULT_POOL_SIZE = "4";
  private static final AtomicInteger counter = new AtomicInteger();
  private static final Duration period =
      Duration.ofMillis(Long.parseLong(System.getProperty("clerk.sampling.rate", DEFAULT_RATE_MS)));
  private static final ScheduledExecutorService executor =
      newScheduledThreadPool(
          Integer.parseInt(System.getProperty("clerk.sampling.workers", DEFAULT_POOL_SIZE)),
          r -> {
            Thread t = new Thread(r, clerkName());
            t.setDaemon(true);
            return t;
          });

  static String clerkName() {
    return String.join("-", "clerk", String.format("%02d", counter.getAndIncrement()));
  }

  private static class MemoryTracker implements Processor<Long, TreeMap<Instant, Long>> {
    private TreeMap<Instant, Long> data = new TreeMap<>();

    @Override
    public void add(Long value) {
      synchronized (data) {
        data.put(Instant.now(), value);
      }
    }

    @Override
    public TreeMap<Instant, Long> process() {
      TreeMap<Instant, Long> data = this.data;
      synchronized (this.data) {
        this.data = new TreeMap<>();
      }
      return data;
    }
  }

  private static TreeMap<Instant, Long> sizeit(Runnable r) {
    Clerk<TreeMap<Instant, Long>> clerk =
        new Clerk<>(
            List.of(() -> Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()),
            new MemoryTracker(),
            new AsynchronousSteadyStateExecutor(period, executor));
    clerk.start();
    r.run();
    clerk.stop();
    return clerk.dump();
  }

  public static void main(String[] args) throws Exception {
    if (args.length < 1) {

    } else {
      Runnable workload =
          () -> {
            try {
              String[] command =
                  args.length == 2 ? args[1].split(" ") : Arrays.copyOfRange(args, 1, args.length);
              new ProcessBuilder(command).start().waitFor();
            } catch (Exception e) {
              logger.info("unable to run command " + args[1]);
              e.printStackTrace();
            }
          };
      logger.info("command \"" + args[1] + "\" consumed " + sizeit(workload) + "B");
    }
  }
}
