package clerk.execution;

import static org.junit.Assert.assertEquals;

import java.time.Duration;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SteadyStateExecutionPolicyTest {
  private SteadyStateExecutionPolicy policy;
  private ScheduledThreadPoolExecutor executor;

  @Before
  public void setUp() {
    policy = new SteadyStateExecutionPolicy(Duration.ofMillis(0));
    executor = new ScheduledThreadPoolExecutor(1);
  }

  @After
  public void tearDown() {
    executor.shutdown();
    executor = null;
    policy = null;
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
    executor.execute(() -> policy.execute(workload, executor));

    for (int i = 0; i < 4; i++) {
      assertEquals(i, executor.getCompletedTaskCount());
      barrier.await();
      while (executor.getCompletedTaskCount() == i) {}
    }
  }
}
