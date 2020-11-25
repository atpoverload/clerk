package clerk.examples;

import clerk.DirectClerk;
import clerk.data.PairStorage;
import clerk.util.ClerkUtil;
import java.time.Duration;
import java.time.Instant;
import java.util.logging.Logger;

/**
 * A clerk that the {@link Duration} since the last time the {@link Stopwatch} was started.
 *
 * <p>If {@code read()} is called while running, the elapsed time since {@code start()} is returned.
 *
 * <p>If {@code read()} is called while not running, the elapsed time between {@code start()} and
 * {@code stop()} is returned.
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

  /** Time a runnable. */
  public static void time(Runnable r, int iters, int runs) {
    Stopwatch stopwatch = new Stopwatch();
    long runtime = Long.MAX_VALUE;
    for (int i = 0; i < runs; i++) {
      long time = 0;
      for (int j = 0; j < iters; j++) {
        stopwatch.start();
        r.run();
        stopwatch.stop();
        time += stopwatch.read().toNanos();
      }
      if (time < runtime) {
        runtime = time;
      }
    }
    logger.info(
        iters + " loops, best of " + runs + ": " + Duration.ofNanos(runtime / iters) + " per loop");
  }

  public static void main(String[] args) throws Exception {
    time(() -> {}, 10000000, 3);
  }
}
