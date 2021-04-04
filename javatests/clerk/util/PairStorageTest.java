package clerk.util;

import static org.junit.Assert.assertEquals;

import clerk.testing.Pair;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class PairStorageTest {
  private PairStorage<String, Pair<String>> storage;

  @Before
  public void setUp() {
    storage =
        new PairStorage<String, Pair<String>>() {
          @Override
          public Pair<String> process() {
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
  public void process_noData() {
    assertEquals(null, storage.process());
  }

  @Test
  public void add_process_insufficientData() {
    storage.add("foo");
    assertEquals(null, storage.process());
  }

  @Test
  public void add_process() {
    storage.add("foo");
    storage.add("bar");
    assertEquals(new Pair("foo", "bar"), storage.process());
  }
}
