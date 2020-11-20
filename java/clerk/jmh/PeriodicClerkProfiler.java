package clerk.jmh;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import org.openjdk.jmh.infra.BenchmarkParams;
import org.openjdk.jmh.profile.ExternalProfiler;
import org.openjdk.jmh.results.BenchmarkResult;
import org.openjdk.jmh.results.Result;

public final class PeriodicClerkProfiler<O extends Result> implements ExternalProfiler {
  private final PeriodicJmhClerk clerk;

  public PeriodicClerkProfiler() {
    this.clerk = new PeriodicJmhClerk();
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
  public void beforeTrial(BenchmarkParams benchmarkParams) {
    clerk.start();
  }

  @Override
  public Collection<? extends Result> afterTrial(
      BenchmarkResult br, long pid, File stdOut, File stdErr) {
    clerk.stop();
    return clerk.read();
  }

  @Override
  public boolean allowPrintOut() {
    return true;
  }

  @Override
  public boolean allowPrintErr() {
    return false;
  }

  @Override
  public String getDescription() {
    return "";
  }
}
