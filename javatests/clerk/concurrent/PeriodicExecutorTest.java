package clerk.concurrent;

import static org.junit.Assert.assertEquals;

import java.time.Duration;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class PeriodicExecutorTest {
  private PeriodicExecutor executor;
  private ScheduledThreadPoolExecutor threadPool;

  @Before
  public void setUp() {
    threadPool = new ScheduledThreadPoolExecutor(1);
    executor = new PeriodicExecutor(threadPool, Duration.ofMillis(0));
  }

  @After
  public void tearDown() {
    threadPool.shutdown();
    threadPool = null;
    executor = null;
  }

  @Test
  public void execute() throws Exception {
    CyclicBarrier barrier = new CyclicBarrier(2);
    Runnable workload =
        () -> {
          try {
            barrier.await();
            barrier.reset();
          } catch (Exception e) {

          }
        };
    executor.execute(workload);

    for (int i = 0; i < 4; i++) {
      assertEquals(i, threadPool.getCompletedTaskCount());
      barrier.await();
      while (threadPool.getCompletedTaskCount() == i) {}
    }
  }
}
