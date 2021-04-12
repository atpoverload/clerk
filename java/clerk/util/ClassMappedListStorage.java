package clerk.util;

import clerk.DataProcessor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** A processor that stores data in map of classes to list of objects of that class. */
public class ClassMappedListStorage<I> implements DataProcessor<I, Map<Class<?>, List<I>>> {
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
  @Override
  public Map<Class<?>, List<I>> process() {
    synchronized (this) {
      HashMap<Class<?>, List<I>> currentData = new HashMap<>();
      for (Class<?> cls : data.keySet()) {
        currentData.put(cls, new ArrayList<>(data.get(cls)));
      }
      return currentData;
    }
  }
}
