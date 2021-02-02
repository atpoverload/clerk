package clerk.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ReturnableListStorageTest {
  private ReturnableListStorage<Integer> storage;

  @Before
  public void setUp() {
    storage = new ReturnableListStorage<>();
  }

  @After
  public void tearDown() {
    storage = null;
  }

  @Test
  public void process_getNoItems() {
    assertTrue(storage.process().isEmpty());
  }

  @Test
  public void addProcess_getValue() {
    storage.add(0);
    List<Integer> data = storage.process();
    assertFalse(data.isEmpty());
    assertEquals(0, (int) data.get(0));
  }

  @Test
  public void addAddProcess_getLastValue() {
    storage.add(0);
    storage.add(1);
    assertEquals(1, (int) storage.process().get(1));
  }
}
