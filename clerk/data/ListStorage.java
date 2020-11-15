package clerk.examples.data;

import clerk.Processor;
import java.util.ArrayList;
import java.util.List;

/** A processor that stores data in a list as added and pops the list when processed. */
public final class ListStorage<I> implements Processor<I, List<I>> {
  private ArrayList<I> data = new ArrayList<>();

  public ListStorage() {}

  /** Adds data with synchronization. */
  @Override
  public final void add(I i) {
    synchronized (data) {
      data.add(i);
    }
  }

  /** Pops and returns data with synchronization. */
  @Override
  public final List<I> process() {
    ArrayList<I> data = this.data;
    synchronized (data) {
      this.data = new ArrayList<>();
    }
    return data;
  }
}
