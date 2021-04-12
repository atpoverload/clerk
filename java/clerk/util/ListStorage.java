package clerk.util;

import clerk.DataProcessor;
import java.util.ArrayList;
import java.util.List;

/** A processor that stores data in a list. */
public class ListStorage<I> implements DataProcessor<I, List<I>> {
  private final ArrayList<I> data = new ArrayList<>();

  /** Adds data to the list. */
  @Override
  public final void add(I i) {
    synchronized (this) {
      data.add(i);
    }
  }

  /** Returns a new list containing the data. */
  @Override
  public List<I> process() {
    synchronized (this) {
      return new ArrayList<>(data);
    }
  }
}
