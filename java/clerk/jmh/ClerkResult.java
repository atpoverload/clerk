package clerk.jmh;

import org.openjdk.jmh.results.AggregationPolicy;
import org.openjdk.jmh.results.Aggregator;
import org.openjdk.jmh.results.Defaults;
import org.openjdk.jmh.results.Result;
import org.openjdk.jmh.results.ResultRole;

public class ClerkResult extends Result<ClerkResult> {
  private static final long serialVersionUID = 6871141606856800453L;

  final String output;
  final String label;

  public ClerkResult(String output, String label) {
    super(
        ResultRole.SECONDARY,
        Defaults.PREFIX + label,
        of(Double.NaN),
        "---",
        AggregationPolicy.AVG);
    this.output = output;
    this.label = label;
  }

  @Override
  protected Aggregator<ClerkResult> getThreadAggregator() {
    return new ClerkResultAggregator();
  }

  @Override
  protected Aggregator<ClerkResult> getIterationAggregator() {
    return new ClerkResultAggregator();
  }

  @Override
  public String toString() {
    return "(text only)";
  }

  @Override
  public String extendedInfo() {
    return output;
  }
}
