package clerk.data;

import static org.junit.Assert.assertEquals;

import java.util.Objects;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class PairStorageTest {
  private PairStorage<Integer, Pair<Integer>> storage;

  private static class Pair<T> {
    private final T first;
    private final T second;

    private Pair(T first, T second) {
      this.first = first;
      this.second = second;
    }

    @Override
    public String toString() {
      return "(" + first + "," + second + ")";
    }

    @Override
    public boolean equals(Object o) {
      if (o instanceof Pair) {
        Pair<T> other = (Pair<T>) o;
        return this.first.equals(other.first) && this.second.equals(other.second);
      }
      return false;
    }

    @Override
    public int hashCode() {
      return Objects.hash(first, second);
    }
  }

  @Before
  public void setUp() {
    storage =
        new PairStorage<Integer, Pair<Integer>>() {
          @Override
          public Pair<Integer> process() {
            if (getFirst() == null || getSecond() == null) {
              return null;
            }
            return new Pair(getFirst(), getSecond());
          }
        };
  }

  @After
  public void tearDown() {
    storage = null;
  }

  @Test
  public void process_getZero() {
    assertEquals(null, storage.process());
  }

  @Test
  public void addProcess_getZero() {
    storage.add(1);
    assertEquals(null, storage.process());
  }

  @Test
  public void addProcess_getValue() {
    storage.add(1);
    storage.add(2);
    assertEquals(new Pair(1, 2), storage.process());
  }
}
