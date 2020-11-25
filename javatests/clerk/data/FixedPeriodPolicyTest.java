package clerk.data;

import static org.junit.Assert.assertEquals;

import java.time.Duration;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class FixedPeriodPolicyTest {
  private FixedPeriodPolicy policy;
  private ScheduledThreadPoolExecutor threadPool;

  @Before
  public void setUp() {
    threadPool = new ScheduledThreadPoolExecutor(1);
    policy = new FixedPeriodPolicy(threadPool, Duration.ofMillis(0));
  }

  @After
  public void tearDown() {
    threadPool.shutdown();
    threadPool = null;
    policy = null;
  }

  @Test
  public void start() throws Exception {
    CyclicBarrier barrier = new CyclicBarrier(2);
    Runnable workload =
        () -> {
          try {
            barrier.await();
            barrier.reset();
          } catch (Exception e) {

          }
        };
    policy.start(workload);

    for (int i = 0; i < 4; i++) {
      assertEquals(i, threadPool.getCompletedTaskCount());
      barrier.await();
      while (threadPool.getCompletedTaskCount() == i) {}
    }
  }

  @Test
  public void stop() throws Exception {
    CyclicBarrier barrier = new CyclicBarrier(2);
    Runnable workload =
        () -> {
          try {
            barrier.await();
            barrier.reset();
          } catch (Exception e) {

          }
        };
    policy.start(workload);
    policy.stop();
    assertEquals(2, threadPool.getTaskCount());
    assertEquals(0, threadPool.getCompletedTaskCount());
  }
}
