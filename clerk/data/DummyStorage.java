package clerk.data;

import clerk.Processor;

/** A processor that stores the last value and returns it. */
public final class DummyStorage<I> implements Processor<I, I> {
  private I data;

  public DummyStorage() {}

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
