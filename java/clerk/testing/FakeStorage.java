package clerk.testing;

import clerk.DataProcessor;

/** A processor that stores and returns the last value sent to {@code add}. */
public final class FakeStorage<I> implements DataProcessor<I, I> {
  private I data;

  /** Replace the data. */
  @Override
  public final void add(I i) {
    data = i;
  }

  /** Return the data. */
  @Override
  public final I process() {
    return data;
  }
}
