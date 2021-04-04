package clerk.util;

import static java.util.Collections.unmodifiableList;

import clerk.DataProcessor;
import java.util.ArrayList;
import java.util.List;

/** A processor that stores data in a list. */
public abstract class ListStorage<I, O> implements DataProcessor<I, O> {
  private final ArrayList<I> data = new ArrayList<>();

  /** Adds data to the list. */
  @Override
  public final void add(I i) {
    data.add(i);
  }

  /** Returns an immutable copy of the data. */
  protected final List<I> getData() {
    return unmodifiableList(data);
  }
}
