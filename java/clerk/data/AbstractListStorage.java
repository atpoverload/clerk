package clerk.data;

import clerk.Processor;
import java.util.ArrayList;

/** A processor that stores data in a list. The data is popped when requested by a user. */
public abstract class AbstractListStorage<I, O> implements Processor<I, O> {
  private ArrayList<I> data = new ArrayList<>();

  /** Adds data with synchronization. */
  @Override
  public final void add(I i) {
    synchronized (data) {
      data.add(i);
    }
  }

  /** Pops and returns data. */
  protected final synchronized ArrayList<I> getData() {
    ArrayList<I> data = this.data;
    synchronized (data) {
      this.data = new ArrayList<>();
    }
    return data;
  }
}
