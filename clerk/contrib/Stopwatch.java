package clerk.contrib;

import clerk.Clerk;
import clerk.Processor;
import clerk.data.DirectExecutor;
import clerk.util.ClerkLogger;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.logging.Logger;

/** A profiler that measures elapsed time between calls as a {@link Duration}. */
public class Stopwatch {
  private static final Logger logger = ClerkLogger.getLogger();

  // inner class to track the two most recent {@code Instant}s
  private static class StopwatchTimer implements Processor<Instant, Duration> {
    private Instant start = Instant.EPOCH;
    private Instant end = Instant.EPOCH;

    @Override
    public void add(Instant timestamp) {
      if (start.equals(Instant.EPOCH)) {
        this.start = timestamp;
      } else if (end.equals(Instant.EPOCH)) {
        this.end = timestamp;
      } else {
        this.start = this.end;
        this.end = timestamp;
      }
    }

    @Override
    public Duration process() {
      return Duration.between(start, end);
    }
  }

  /** Returns the elapsed time of a workload. */
  public static Duration time(Runnable workload) {
    Clerk<Duration> clerk =
        new Clerk<>(List.of(Instant::now), new StopwatchTimer(), new DirectExecutor());
    clerk.start();
    workload.run();
    clerk.stop();
    return clerk.dump();
  }

  /** Times a workload similar to python's timeit module. */
  public static void timeIt(Runnable workload, int iters, int runs) {
    long runtime = Long.MAX_VALUE;
    for (int i = 0; i < runs; i++) {
      long time = 0;
      for (int j = 0; j < iters; j++) {
        time += time(workload).toNanos();
      }
      if (time < runtime) {
        runtime = time;
      }
    }
    logger.info(
        iters + " loops, best of " + runs + ": " + Duration.ofNanos(runtime / iters) + " per loop");
  }

  public static void main(String[] args) throws Exception {
    if (args.length == 0) {
      timeIt(() -> {}, 10000000, 3);
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
      logger.info("ran \"" + String.join(" ", args) + "\" in " + time(workload));
    }
  }
}
