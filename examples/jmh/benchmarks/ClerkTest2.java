package clerk.examples;

import static java.lang.Math.PI;
import static java.lang.Math.log;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.infra.Blackhole;

public class ClerkTest2 {
  @Benchmark
  public void test(Blackhole bh) {
    bh.consume(log(PI) * log(PI));
  }
}