package clerk.profilers;

import clerk.Processor;
import java.util.Map;
import java.util.TreeMap;

public abstract class DataSorter<K, V> implements Processor<V, Map<K, V>> {
  private TreeMap<K, V> data = new TreeMap<>();

  protected abstract K getKey(V value);

  @Override
  public void add(V value) {
    synchronized (this.data) {
      this.data.put(getKey(value), value);
    }
  }

  @Override
  public Map<K, V> process() {
    Map<K, V> data = this.data;
    synchronized (this.data) {
      this.data = new TreeMap<>();
    }
    return data;
  }
}
