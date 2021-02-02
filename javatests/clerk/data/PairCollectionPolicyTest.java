package clerk.data;

import static java.util.concurrent.Executors.newSingleThreadScheduledExecutor;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.ScheduledExecutorService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class PairCollectionPolicyTest {
  private PairStorage<Object, Boolean> storage;
  private ScheduledExecutorService executor;
  private PairCollectionPolicy policy;

  @Before
  public void setUp() {
    storage =
        new PairStorage<Object, Boolean>() {
          @Override
          public Boolean process() {
            if (getFirst() != null && getSecond() != null) {
              return true;
            }
            return false;
          }
        };
    executor = newSingleThreadScheduledExecutor();
    policy = new PairCollectionPolicy(executor);
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
    policy.stop();
    while (!storage.process()) {}
    assertTrue(storage.process());
  }
}
