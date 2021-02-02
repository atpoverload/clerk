package clerk.data;

import static java.util.concurrent.Executors.newSingleThreadScheduledExecutor;
import static org.junit.Assert.assertEquals;

import clerk.testing.data.DummyStorage;
import java.util.concurrent.ScheduledExecutorService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SingleCollectionPolicyTest {
  private DummyStorage<Integer> storage;
  private ScheduledExecutorService executor;
  private SingleCollectionPolicy policy;

  @Before
  public void setUp() {
    storage = new DummyStorage<>();
    storage.add(0);
    executor = newSingleThreadScheduledExecutor();
    policy = new SingleCollectionPolicy(executor);
  }

  @After
  public void tearDown() {
    storage = null;
    executor = null;
    policy = null;
  }

  @Test
  public void collect() throws Exception {
    policy.collect(() -> 1, storage);
    while (storage.process() == 0) {}
    assertEquals(1, (int) storage.process());
  }
}
