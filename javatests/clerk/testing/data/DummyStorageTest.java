package clerk.testing.data;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DummyStorageTest {
  private DummyStorage<Integer> storage;

  @Before
  public void setUp() {
    storage = new DummyStorage<>();
  }

  @After
  public void tearDown() {
    storage = null;
  }

  @Test
  public void process_getNull() {
    assertEquals(null, storage.process());
  }

  @Test
  public void addProcess_getValue() {
    storage.add(0);
    assertEquals(0, (int) storage.process());
  }

  @Test
  public void addAddProcess_getLastValue() {
    storage.add(0);
    storage.add(1);
    assertEquals(1, (int) storage.process());
  }
}
