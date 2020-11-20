package clerk.benchmarks;

import static java.lang.Math.PI;
import static java.lang.Math.log;

import clerk.jmh.PeriodicClerkProfiler;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

public class PeriodicClerkBenchmark {
  @Benchmark
  public void test(Blackhole bh) {
    bh.consume(log(PI));
  }

  public static void main(String[] args) throws RunnerException {
    Options opt =
        new OptionsBuilder()
            .include(PeriodicClerkBenchmark.class.getSimpleName())
            .addProfiler(PeriodicClerkProfiler.class)
            .build();
    new Runner(opt).run();
  }
}
