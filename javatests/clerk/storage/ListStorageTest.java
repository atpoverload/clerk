package clerk.storage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ListStorageTest {
  private ListStorage<String, List<String>> storage;

  @Before
  public void setUp() {
    storage =
        new ListStorage<String, List<String>>() {
          @Override
          public List<String> process() {
            return getData();
          }
        };
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
    storage.add("foo");
    List<String> data = storage.process();
    assertFalse(data.isEmpty());
    assertEquals("foo", data.get(0));
  }

  @Test
  public void addAddProcess_getLastValue() {
    storage.add("foo");
    storage.add("bar");
    assertEquals("bar", storage.process().get(1));
  }
}
