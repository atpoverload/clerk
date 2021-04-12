package clerk.util;

import clerk.DataProcessor;
import java.util.HashMap;
import java.util.Map;

/** A processor that stores data in map of classes to objects of that class. */
public class ClassMappedStorage<I> implements DataProcessor<I, Map<Class<?>, I>> {
  private final HashMap<Class<?>, I> data = new HashMap<>();

  /** Sets the data in the map. */
  @Override
  public final void add(I i) {
    synchronized (this) {
      data.put(i.getClass(), i);
    }
  }

  /** Returns a copy of the map. */
  @Override
  public Map<Class<?>, I> process() {
    synchronized (this) {
      return new HashMap<>(data);
    }
  }
}
