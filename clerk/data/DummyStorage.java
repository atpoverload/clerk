package clerk.data;

import clerk.Processor;

/** A processor that stores the last value and returns it. */
public final class DummyStorage<I> implements Processor<I, I> {
  private I dummy;

  public DummyStorage() {}

  /** Replace the dummy. */
  @Override
  public final void add(I i) {
    dummy = i;
  }

  /** Return the dummy. */
  @Override
  public final I process() {
    return dummy;
  }
}
