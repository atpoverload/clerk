package clerk.examples.jmh;

import static java.util.concurrent.Executors.newSingleThreadScheduledExecutor;

import clerk.Clerk;
import clerk.storage.ListStorage;
import clerk.util.FixedPeriodClerk;
import java.io.File;
import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import org.openjdk.jmh.infra.BenchmarkParams;
import org.openjdk.jmh.profile.ExternalProfiler;
import org.openjdk.jmh.results.BenchmarkResult;
import org.openjdk.jmh.results.Result;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

/** A profiler that wraps around a clerk. */
public final class ClerkProfiler implements ExternalProfiler {
  private static final ScheduledExecutorService executor = newSingleThreadScheduledExecutor();

  private static long[] computeDiff(List<Instant> timestamps) {
    long[] diffs = new long[timestamps.size() - 1];
    for (int i = 0; i < timestamps.size() - 1; i++) {
      diffs[i] = Duration.between(timestamps.get(i), timestamps.get(i + 1)).toNanos();
    }
    return diffs;
  }

  private static class SamplingClerk extends FixedPeriodClerk {
    private SamplingClerk() {
      super(
          Instant::now,
          new ListStorage<Instant, Collection<? extends Result>>() {
            @Override
            public Collection<? extends Result> process() {
              return List.of(new ClerkResult(computeDiff(getData())));
            }
          },
          executor,
          Duration.ofNanos(500000));
    }
  }

  private Clerk<Collection<? extends Result>> clerk;

  /** Create a new clerk and start it. */
  @Override
  public final void beforeTrial(BenchmarkParams benchmarkParams) {
    clerk = new SamplingClerk();
    clerk.start();
  }

  /** Stops eflect and transforms the data into an {@link EflectResult}. */
  @Override
  public final Collection<? extends Result> afterTrial(
      BenchmarkResult br, long pid, File stdOut, File stdErr) {
    clerk.stop();
    Collection<? extends Result> results = clerk.read();
    clerk = null;
    return results;
  }

  @Override
  public String getDescription() {
    return "clerk";
  }

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
    Options opt = new OptionsBuilder().addProfiler(ClerkProfiler.class).build();
    new Runner(opt).run();
    executor.shutdown();
  }
}
