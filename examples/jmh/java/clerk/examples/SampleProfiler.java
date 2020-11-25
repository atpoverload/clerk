package clerk.examples;

import clerk.FixedPeriodClerk;
import clerk.data.AbstractListStorage;
import java.io.File;
import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.openjdk.jmh.infra.BenchmarkParams;
import org.openjdk.jmh.profile.ExternalProfiler;
import org.openjdk.jmh.results.BenchmarkResult;
import org.openjdk.jmh.results.Result;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

/** A profiler that reports the number of samples collected by a clerk. */
public final class SampleProfiler implements ExternalProfiler {
  private static final int period = Integer.parseInt(System.getProperty("clerk.jmh.period", "4"));

  private static Collection<Result> toResult(List<Instant> data) {
    int samples = data.size();
    long expected = 1;
    if (period > 0) {
      expected = Duration.between(data.get(0), data.get(samples - 1)).toMillis() / period;
    }
    return List.of(new SamplingResult(samples, expected));
  }

  private final FixedPeriodClerk<Collection<? extends Result>> clerk;

  public SampleProfiler() {
    clerk =
        new FixedPeriodClerk(
            () -> Instant.now(),
            new AbstractListStorage<Instant, Collection<Result>>() {
              @Override
              public Collection<Result> process() {
                return toResult(getData());
              }
            },
            Duration.ofMillis(period));
  }

  /** Starts the clerk. */
  @Override
  public final void beforeTrial(BenchmarkParams benchmarkParams) {
    clerk.start();
  }

  /** Stops the clerk. */
  @Override
  public final Collection<? extends Result> afterTrial(
      BenchmarkResult br, long pid, File stdOut, File stdErr) {
    clerk.stop();
    return clerk.read();
  }

  @Override
  public String getDescription() {
    return "clerk-sample-counter";
  }

  // Default implementations for ExternalProfiler interface
  @Override
  public Collection<String> addJVMInvokeOptions(BenchmarkParams params) {
    return Collections.emptyList();
  }

  @Override
  public Collection<String> addJVMOptions(BenchmarkParams params) {
    return Collections.emptyList();
  }

  @Override
  public boolean allowPrintOut() {
    return true;
  }

  @Override
  public boolean allowPrintErr() {
    return false;
  }

  // driver for this profiler
  public static void main(String[] args) throws RunnerException {
    Options opt = new OptionsBuilder().addProfiler(SampleProfiler.class).build();
    new Runner(opt).run();
  }
}
