package clerk.contrib.inject;

import clerk.Clerk;
import clerk.inject.ClerkModule;
import clerk.inject.DirectSamplingModule;
import clerk.util.ClerkLogger;
import dagger.Component;
import java.time.Duration;
import java.util.logging.Logger;

/** A profiler that measures elapsed time between calls as a {@link Duration}. */
public class Stopwatch {
  private static final Logger logger = ClerkLogger.getLogger();

  @Component(modules = {ClerkModule.class, StopwatchModule.class, DirectSamplingModule.class})
  interface ClerkFactory {
    Clerk newClerk();
  }

  private static final ClerkFactory clerkFactory = DaggerStopwatch_ClerkFactory.builder().build();

  private Clerk<Duration> clerk;

  /** Returns the elapsed time of a workload. */
  public static Duration time(Runnable workload) {
    Clerk<Duration> clerk = clerkFactory.newClerk();
    clerk.start();
    workload.run();
    clerk.stop();
    return clerk.dump();
  }

  /** Times a workload similar to python's timeit module. */
  public static void timeit(Runnable workload, int iters, int runs) {
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
      timeit(() -> {}, 10000000, 3);
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
