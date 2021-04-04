package clerk.util;

import clerk.DataProcessor;

/** A processor that only stores the last 2 pieces of data added. */
public abstract class PairStorage<I, O> implements DataProcessor<I, O> {
  private I first;
  private I second;

  /**
   * Sets the data. If there is no data, first is set. If there is no second piece of data, second
   * is set. Otherwise, the first becomes second and second becomes the new value.
   */
  @Override
  public final void add(I i) {
    if (first == null) {
      first = i;
    } else if (second == null) {
      second = i;
    } else {
      first = second;
      second = i;
    }
  }

  /** Get the first piece of data. */
  protected final I getFirst() {
    return first;
  }

  /** Get the second piece of data. */
  protected final I getSecond() {
    return second;
  }
}
