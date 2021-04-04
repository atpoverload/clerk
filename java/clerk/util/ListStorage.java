package clerk.util;

import clerk.DataProcessor;
import java.util.ArrayList;
import java.util.List;

/** A processor that stores data in a list. */
public final class ListStorage<I> implements DataProcessor<I, List<I>> {
  private final ArrayList<I> data = new ArrayList<>();

  /** Adds data to the list. */
  @Override
  public final void add(I i) {
    data.add(i);
  }

  /** Returns the stored data. */
  @Override
  public final List<I> process() {
    return data;
  }
}
