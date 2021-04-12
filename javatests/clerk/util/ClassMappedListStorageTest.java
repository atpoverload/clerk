package clerk.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ClassMappedListStorageTest {
  private class A {}

  private class B extends A {}

  private class C extends A {}

  private ClassMappedListStorage<A> storage;
  private B b = new B();
  private C c = new C();

  @Before
  public void setUp() {
    storage = new ClassMappedListStorage<>();
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
    storage.add(b);
    Map<Class<?>, List<A>> data = storage.process();
    assertFalse(data.isEmpty());
    assertEquals(b, data.get(B.class).get(0));
    assertFalse(data.containsKey(C.class));
  }

  @Test
  public void addAddProcess_getLastValue() {
    storage.add(b);
    storage.add(c);
    Map<Class<?>, List<A>> data = storage.process();
    assertEquals(b, data.get(B.class).get(0));
    assertEquals(c, data.get(C.class).get(0));
  }
}
