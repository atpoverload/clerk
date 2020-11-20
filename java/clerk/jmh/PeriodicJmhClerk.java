package clerk.jmh;

import clerk.PeriodicClerk;
import clerk.data.AbstractListStorage;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A clerk that the {@link Duration} since the last time the {@link Stopwatch} was started.
 *
 * <p>If {@code read()} is called while running, the elapsed time since {@code start()} is returned.
 *
 * <p>If {@code read()} is called while not running, the elapsed time between {@code start()} and
 * {@code stop()} is returned.
 *
 * <p>Note that while this is similar to python's timeit module, it is not performance optimized.
 */
public class PeriodicJmhClerk extends PeriodicClerk<Collection<ClerkResult>> {
  public PeriodicJmhClerk() {
    super(
        () -> Instant.now(),
        new AbstractListStorage<Instant, Collection<ClerkResult>>() {
          @Override
          public Collection<ClerkResult> process() {
            ArrayList<Instant> data = getData();

            int samples = data.size();
            Duration duration = Duration.between(data.get(0), data.get(samples - 1));

            String[] results =
                new String[] {
                  getClass().getSimpleName() + " results:",
                  " - collected " + samples + " samples",
                  " - benchmark ran in " + duration,
                  ""
                };

            String message = String.join(System.lineSeparator(), results);
            return List.of(new ClerkResult(message, getClass().getSimpleName()));
          }
        },
        Duration.ofMillis(1));
  }
}
