package clerk.storage;

import clerk.DataProcessor;
import java.util.HashMap;
import java.util.Map;

/** A processor that stores data in map of classes to objects of that class. */
public abstract class ClassMappedStorage<I, O> implements DataProcessor<I, O> {
  private final HashMap<Class<?>, I> data = new HashMap<>();

  /** Sets the data in the map. */
  @Override
  public final void add(I i) {
    synchronized (this) {
      data.put(i.getClass(), i);
    }
  }

  /** Returns a copy of the map. */
  protected Map<Class<?>, I> getData() {
    synchronized (this) {
      HashMap<Class<?>, I> snapshot = new HashMap<>();
      for (Class<?> key : data.keySet()) {
        snapshot.put(key, data.get(key));
      }
      return snapshot;
    }
  }
}
