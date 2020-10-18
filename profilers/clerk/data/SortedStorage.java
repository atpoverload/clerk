package clerk.data;

import static java.util.Collections.unmodifiableList;

import clerk.Processor;
import java.util.List;
import java.util.TreeSet;

public final class SortedStorage<T extends Comparable<T>> implements Processor<T, List<T>> {
  private final TreeSet<T> data = new TreeSet<>();

  @Override
  public void accept(T t) {
    synchronized (data) {
      data.put(t);
    }
  }

  @Override
  public List<T> get() {
    List<T> data = unmodifiableList(this.data);
    synchronized (data) {
      this.data.clear();
    }
    return data;
  }
}
