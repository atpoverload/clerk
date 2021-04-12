package clerk.storage;

import clerk.DataProcessor;

/** A processor that stores and returns the last value sent to {@code add}. */
public abstract class SingleStorage<I, O> implements DataProcessor<I, O> {
  private I data;

  /** Replace the data. */
  @Override
  public final void add(I i) {
    data = i;
  }

  /** Return the data. */
  protected I getData() {
    return data;
  }
}
