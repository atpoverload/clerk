package clerk.jmh;

import clerk.Clerk;
import java.io.File;
import java.util.Collection;
import java.util.Collections;
import org.openjdk.jmh.infra.BenchmarkParams;
import org.openjdk.jmh.profile.ExternalProfiler;
import org.openjdk.jmh.results.BenchmarkResult;
import org.openjdk.jmh.results.Result;

/** An {@link ExternalProfiler} that collects data using a {@link Clerk}. */
public abstract class ClerkProfiler implements ExternalProfiler {
  /** Returns the underlying clerk used to collect data. */
  protected abstract Clerk<Collection<? extends Result>> getClerk();

  // final methods that control the clerk
  /** Starts the clerk. */
  @Override
  public final void beforeTrial(BenchmarkParams benchmarkParams) {
    getClerk().start();
  }

  /** Stops the clerk. */
  @Override
  public final Collection<? extends Result> afterTrial(
      BenchmarkResult br, long pid, File stdOut, File stdErr) {
    getClerk().stop();
    return getClerk().read();
  }

  /** Returns the name of the clerk. */
  @Override
  public String getDescription() {
    return getClerk().getClass().getSimpleName();
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
}
