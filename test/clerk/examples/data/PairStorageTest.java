package clerk.examples.data;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class PairStorageTest {
  private PairStorage<Integer, Integer> storage;

  @Before
  public void setUp() {
    storage =
        new PairStorage<>() {
          @Override
          public Integer process() {
            if (getFirst() == null || getSecond() == null) {
              return 0;
            }
            return getFirst() + getSecond();
          }
        };
  }

  @After
  public void tearDown() {
    storage = null;
  }

  @Test
  public void process_getZero() {
    assertEquals(0, (int) storage.process());
  }

  @Test
  public void addProcess_getZero() {
    storage.add(1);
    assertEquals(0, (int) storage.process());
  }

  @Test
  public void addProcess_getValue() {
    storage.add(1);
    storage.add(1);
    assertEquals(2, (int) storage.process());
  }
}
