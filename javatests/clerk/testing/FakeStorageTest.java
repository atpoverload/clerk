package clerk.testing;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class FakeStorageTest {
  private FakeStorage<String> storage;

  @Before
  public void setUp() {
    storage = new FakeStorage<>();
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
    storage.add("foo");
    assertEquals("foo", storage.process());
  }

  @Test
  public void addAddProcess_getLastValue() {
    storage.add("foo");
    storage.add("bar");
    assertEquals("bar", storage.process());
  }
}
