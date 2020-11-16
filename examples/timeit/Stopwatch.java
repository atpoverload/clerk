package clerk.examples;

import clerk.clerks.DirectClerk;
import clerk.data.PairStorage;
import clerk.util.ClerkUtil;
import java.time.Duration;
import java.time.Instant;
import java.util.logging.Logger;

/**
 * A clerk that returns a {@link Duration} from {@code read()} representing elapsed time since last
 * call to {@code start()}.
 *
 * <p>If {@code read()} is called while running, the elapsed time since {@code start()} is returned.
 *
 * <p>If {@code read()} is called while not running, the elapsed time between {@code start()} and
 * {@code stop()} is returned.
 *
 * <p>Note that while this is similar to python's timeit module, it is not performance optimized.
 */
public class Stopwatch extends DirectClerk<Duration> {
  private static final Logger logger = ClerkUtil.getLogger();

  public Stopwatch() {
    super(
        () -> Instant.now(),
        new PairStorage<Instant, Duration>() {
          @Override
          public Duration process() {
            return Duration.between(getFirst(), getSecond());
          }
        });
  }

  /** Returns the elapsed time of a workload. */
  public static Duration time(Runnable workload) {
    Stopwatch stopwatch = new Stopwatch();
    stopwatch.start();
    workload.run();
    stopwatch.stop();
    return stopwatch.read();
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
