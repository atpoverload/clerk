package clerk.examples.jmh;

import static org.openjdk.jmh.results.AggregationPolicy.AVG;
import static org.openjdk.jmh.results.ResultRole.SECONDARY;

import java.util.ArrayList;
import java.util.Collection;
import org.openjdk.jmh.results.Aggregator;
import org.openjdk.jmh.results.Result;
import org.openjdk.jmh.util.ListStatistics;

/** A profiler that wraps around a clerk. */
class ClerkResult extends Result {
  private final long[] data;

  ClerkResult(long[] data) {
    super(SECONDARY, "clerk", new ListStatistics(data), "ms", AVG);
    this.data = data;
  }

  @Override
  protected Aggregator<ClerkResult> getThreadAggregator() {
    return new ClerkResultAggregator();
  }

  @Override
  protected Aggregator<ClerkResult> getIterationAggregator() {
    return new ClerkResultAggregator();
  }

  static class ClerkResultAggregator implements Aggregator<ClerkResult> {
    @Override
    public ClerkResult aggregate(Collection<ClerkResult> results) {
      ArrayList<Long> values = new ArrayList();
      for (ClerkResult r : results) {
        for (long value : r.data) {
          values.add(value);
        }
      }
      return new ClerkResult(values.stream().mapToLong(i -> i).toArray());
    }
  }
}
