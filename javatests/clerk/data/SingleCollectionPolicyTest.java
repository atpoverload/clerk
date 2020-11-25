package clerk.data;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SingleCollectionPolicyTest {
  private SingleCollectionPolicy policy;
  private ScheduledThreadPoolExecutor threadPool;

  @Before
  public void setUp() {
    threadPool = new ScheduledThreadPoolExecutor(1);
    policy = new SingleCollectionPolicy(threadPool);
  }

  @After
  public void tearDown() {
    policy = null;
  }

  @Test
  public void start() throws Exception {
    policy.start(
        () -> {
          return;
        });
    while (threadPool.getCompletedTaskCount() == 0) {}
    assertEquals(1, threadPool.getCompletedTaskCount());
  }
}
