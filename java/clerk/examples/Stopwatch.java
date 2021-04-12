package clerk.examples;

import clerk.storage.PairStorage;
import clerk.util.DirectClerk;
import java.time.Duration;
import java.time.Instant;

public final class Stopwatch extends DirectClerk<Duration> {
  public Stopwatch() {
    super(
        Instant::now,
        new PairStorage<Instant, Duration>() {
          @Override
          public Duration process() {
            return Duration.between(getFirst(), getSecond());
          }
        });
  }

  public static void main(String[] args) throws Exception {
    Stopwatch stopwatch = new Stopwatch();

    stopwatch.start();
    Thread.sleep(1000);
    stopwatch.stop();
    System.out.println("slept for " + stopwatch.read());
  }
}
