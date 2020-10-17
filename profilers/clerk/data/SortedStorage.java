package clerk.data;

import static java.util.Collections.unmodifiableSet;

import clerk.Processor;
import java.util.Set;
import java.util.TreeSet;

public final class SortedStorage<T extends Comparable<T>> implements Processor<T, Set<T>> {
  private final TreeSet<T> data = new TreeSet<>();

  @Override
  public void accept(T t) {
    synchronized (data) {
      data.put(t);
    }
  }

  @Override
  public Set<T> get() {
    Set<T> data = unmodifiableSet(this.data);
    synchronized (data) {
      this.data.clear()
    }
    return data;
  }
}
