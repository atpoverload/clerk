package clerk.examples;

import static org.openjdk.jmh.results.AggregationPolicy.AVG;
import static org.openjdk.jmh.results.Defaults.PREFIX;
import static org.openjdk.jmh.results.ResultRole.SECONDARY;

import java.util.Collection;
import org.openjdk.jmh.results.Aggregator;
import org.openjdk.jmh.results.Result;

/** A result that reports the number of collected vs expected samples from a trial. */
class SamplingResult extends Result<SamplingResult> {
  private final int samples;
  private final long expected;

  public SamplingResult(int samples, long expected) {
    super(
        SECONDARY,
        PREFIX + "clerk-sampling",
        of(100 * (double) samples / expected),
        "% collected",
        AVG);
    this.samples = samples;
    this.expected = expected;
  }

  @Override
  protected Aggregator<SamplingResult> getThreadAggregator() {
    return new SamplingResultAggregator();
  }

  @Override
  protected Aggregator<SamplingResult> getIterationAggregator() {
    return new SamplingResultAggregator();
  }

  @Override
  public String extendedInfo() {
    return "collected " + samples + "/" + expected + " samples";
  }

  private class SamplingResultAggregator implements Aggregator<SamplingResult> {
    @Override
    public SamplingResult aggregate(Collection<SamplingResult> results) {
      int samples = 0;
      long expected = 0;
      for (SamplingResult r : results) {
        samples += r.samples;
        expected += r.expected;
      }
      return new SamplingResult(samples, expected);
    }
  }
}
