package clerk.util;

import java.util.List;

/** A processor that aggregates {@link ListStorage} data into a single value. */
public abstract class SimpleAggregator<I, O> extends ListStorage<I, O> {
  /** Returns the aggregated data. */
  @Override
  public final O process() {
    return aggregate(getData());
  }

  /** Reduces the data to a single value. */
  protected abstract O aggregate(List<I> data);
}
