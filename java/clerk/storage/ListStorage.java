package clerk.storage;

import clerk.DataProcessor;
import java.util.ArrayList;
import java.util.List;

/** A processor that stores data in a list. */
public abstract class ListStorage<I, O> implements DataProcessor<I, O> {
  private final ArrayList<I> data = new ArrayList<>();

  /** Adds data to the list. */
  @Override
  public final void add(I i) {
    synchronized (this) {
      data.add(i);
    }
  }

  /** Returns a new list containing the data. */
  protected List<I> getData() {
    synchronized (this) {
      ArrayList<I> snapshot = new ArrayList<>();
      for (I i : data) {
        snapshot.add(i);
      }
      return snapshot;
    }
  }
}
