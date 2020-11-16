package clerk.util;

import static org.junit.Assert.assertEquals;

import clerk.data.DummyStorage;
import java.time.Duration;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import org.junit.Test;

public class ClerkUtilTest {
  @Test
  public void pipeTest_succeed() throws Exception {
    DummyStorage<Integer> processor = new DummyStorage<>();
    ClerkUtil.pipe(() -> 1, processor);
    assertEquals(1, (int) processor.process());
  }

  // erasure is breaking this test
  // @Test
  // public void pipeTest_fail() throws Exception {
  //   DummyStorage<Instant> processor = new DummyStorage<>();
  //   assertThrows(ClassCastException.class, () -> ClerkUtil.pipe(() -> Duration.ZERO, processor));
  //   assertEquals(null, processor.process());
  // }

  @Test
  public void runAndRescheduleTest() throws Exception {
    ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);

    CyclicBarrier barrier = new CyclicBarrier(2);
    Runnable workload =
        () -> {
          try {
            barrier.await();
            barrier.reset();
          } catch (Exception e) {

          }
        };
    executor.execute(() -> ClerkUtil.runAndReschedule(workload, executor, Duration.ZERO));

    for (int i = 0; i < 4; i++) {
      assertEquals(i, executor.getCompletedTaskCount());
      barrier.await();
      while (executor.getCompletedTaskCount() == i) {}
    }

    executor.shutdown();
  }
}
