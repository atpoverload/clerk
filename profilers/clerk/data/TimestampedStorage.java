package clerk.data;

import clerk.Processor;
import java.util.TreeSet;

public final class SortedStorage<T extends Comparable<T>> implements Processor<T, Iterable<T>> {
  private final TreeSet<T> data = new TreeSet<>();

  @Override
  public void accept(T t) {
    data.put(t);
  }

  @Override
  public Iterable<T> get() {
    return data;
  }
}
