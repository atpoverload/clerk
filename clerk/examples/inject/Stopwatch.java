package clerk.examples.inject;

import clerk.Clerk;
import clerk.inject.SynchronousClerkModule;
import clerk.util.ClerkLogger;
import dagger.Component;
import java.time.Duration;
import java.util.logging.Logger;

/**
 * A class that provides a clerk that returns a {@link Duration} from {@code read()} representing
 * elapsed time since last call to {@code start()}.
 *
 * <p>If {@code read()} is called while running, the elapsed time since {@code start()} is returned.
 *
 * <p>If {@code read()} is called while not running, the elapsed time between {@code start()} and
 * {@code stop()} is returned.
 */
public class Stopwatch {
  @Component(modules = {SynchronousClerkModule.class, StopwatchModule.class})
  interface ClerkFactory {
    Clerk newClerk();
  }

  private static final ClerkFactory clerkFactory = DaggerStopwatch_ClerkFactory.builder().build();

  public static Clerk<Duration> newStopwatch() {
    return clerkFactory.newClerk();
  }

  private static final Logger logger = ClerkLogger.getLogger();

  /** Returns the elapsed time of a workload. */
  public static Duration time(Runnable workload) {
    Clerk<Duration> clerk = newStopwatch();
    clerk.start();
    workload.run();
    clerk.stop();
    return clerk.read();
  }

  /** Times a workload similar to python's timeit module. */
  public static void time(Runnable workload, int iters, int runs) {
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
      time(() -> {}, 10000000, 3);
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
