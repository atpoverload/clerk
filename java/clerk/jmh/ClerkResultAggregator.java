package clerk.jmh;

import java.util.Collection;
import org.openjdk.jmh.results.Aggregator;

class ClerkResultAggregator implements Aggregator<ClerkResult> {
  @Override
  public ClerkResult aggregate(Collection<ClerkResult> results) {
    StringBuilder output = new StringBuilder();
    String label = null;
    for (ClerkResult r : results) {
      output.append(r.output);
      if (label == null) {
        label = r.label;
      } else if (!label.equalsIgnoreCase(r.label)) {
        throw new IllegalStateException(
            "Trying to aggregate different labels: " + label + ", " + r.label);
      }
    }
    return new ClerkResult(output.toString(), label);
  }
}
