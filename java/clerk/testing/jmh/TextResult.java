package clerk.testing.jmh;

import static org.openjdk.jmh.results.AggregationPolicy.AVG;
import static org.openjdk.jmh.results.Defaults.PREFIX;
import static org.openjdk.jmh.results.ResultRole.SECONDARY;

import java.util.Collection;
import org.openjdk.jmh.results.Aggregator;
import org.openjdk.jmh.results.Result;

/** Copy of jmh's {@link TextResult} that I can't import for some reason. */
public class TextResult extends Result<TextResult> {
  final String output;
  final String label;

  public TextResult(String output, String label) {
    super(SECONDARY, PREFIX + label, of(Double.NaN), "---", AVG);
    this.output = output;
    this.label = label;
  }

  @Override
  protected Aggregator<TextResult> getThreadAggregator() {
    return new TextResultAggregator();
  }

  @Override
  protected Aggregator<TextResult> getIterationAggregator() {
    return new TextResultAggregator();
  }

  @Override
  public String toString() {
    return "(text only)";
  }

  @Override
  public String extendedInfo() {
    return output;
  }

  private class TextResultAggregator implements Aggregator<TextResult> {
    @Override
    public TextResult aggregate(Collection<TextResult> results) {
      StringBuilder output = new StringBuilder();
      String label = null;
      for (TextResult r : results) {
        output.append(r.output);
        if (label == null) {
          label = r.label;
        } else if (!label.equalsIgnoreCase(r.label)) {
          throw new IllegalStateException(
              "Trying to aggregate different labels: " + label + ", " + r.label);
        }
      }
      return new TextResult(output.toString(), label);
    }
  }
}
