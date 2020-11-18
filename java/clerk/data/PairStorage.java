package clerk.data;

import clerk.Processor;

/** A processor that only stores the last 2 pieces of data added. */
public abstract class PairStorage<I, O> implements Processor<I, O> {
  private I first;
  private I second;

  public PairStorage() {}

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

  protected final I getFirst() {
    return first;
  }

  protected final I getSecond() {
    return second;
  }
}
