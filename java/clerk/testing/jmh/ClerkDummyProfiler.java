package clerk.testing.jmh;

import clerk.PeriodicClerk;
import clerk.data.AbstractListStorage;
import clerk.jmh.WrappedClerkProfiler;
import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import org.openjdk.jmh.results.Result;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

public class ClerkDummyProfiler extends WrappedClerkProfiler {
  private static Collection<Result> toTextResult(List<Instant> data) {
    int samples = data.size();
    Duration duration = Duration.between(data.get(0), data.get(samples - 1));
    String[] results = new String[] {"collected " + samples + " samples in " + duration};
    String message = String.join(System.lineSeparator(), results);
    return List.of(new TextResult(message, "clerk-dummy-profiler"));
  }

  public ClerkDummyProfiler() {
    super(
        new PeriodicClerk(
            () -> Instant.now(),
            new AbstractListStorage<Instant, Collection<Result>>() {
              @Override
              public Collection<Result> process() {
                return toTextResult(getData());
              }
            },
            Duration.ofMillis(0)));
  }

  public static void main(String[] args) throws RunnerException {
    Options opt = new OptionsBuilder().addProfiler(ClerkDummyProfiler.class).build();
    new Runner(opt).run();
  }
}
