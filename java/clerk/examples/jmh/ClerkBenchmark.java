package clerk.examples.jmh;

import java.util.concurrent.TimeUnit;
import org.openjdk.jmh.annotations.Benchmark;

public class ClerkBenchmark {
  @Benchmark
  public void doNothing() {}

  @Benchmark
  public void sleep() throws Exception {
    TimeUnit.MICROSECONDS.sleep(100);
  }

  @Benchmark
  public double logPi() {
    return Math.log(Math.PI);
  }
}
