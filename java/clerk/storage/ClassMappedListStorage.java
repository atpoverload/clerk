package clerk.storage;

import clerk.DataProcessor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** A processor that stores data in map of classes to list of objects of that class. */
public abstract class ClassMappedListStorage<I, O> implements DataProcessor<I, O> {
  private final HashMap<Class<?>, List<I>> data = new HashMap<>();

  /** Adds data to the correct list. */
  @Override
  public final void add(I i) {
    synchronized (this) {
      data.computeIfAbsent(i.getClass(), k -> new ArrayList<>());
      data.get(i.getClass()).add(i);
    }
  }

  /** Returns a map of copied lists. */
  protected Map<Class<?>, List<I>> getData() {
    synchronized (this) {
      HashMap<Class<?>, List<I>> snapshot = new HashMap<>();
      for (Class<?> cls : data.keySet()) {
        snapshot.put(cls, new ArrayList<>());
        for (I i : data.get(cls)) {
          snapshot.get(cls).add(i);
        }
      }
      return snapshot;
    }
  }
}
