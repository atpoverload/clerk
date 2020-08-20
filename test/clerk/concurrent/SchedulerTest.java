package clerk.concurrent;

import static org.junit.Assert.assertEquals;

import dagger.Component;
import dagger.Module;
import dagger.Provides;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicInteger;
import javax.inject.Inject;
import org.junit.Before;
import org.junit.Test;

// THIS NEEDS TO BE FIXED!!!!!!!
public class SchedulerTest {
  @Component(modules = {SerialSchedulingModule.class})
  interface TestComponent {
    void inject(SchedulerTest test);
  }

  private static final int ITERATIONS = 10;
  private static final Duration SLEEP_TIME = Duration.ofMillis(250);

  @Inject Scheduler scheduler;

  private AtomicInteger counter;
  private CyclicBarrier barrier;
  private Runnable task;

  @Before
  public void setUp() {
    DaggerSchedulerTest_TestComponent.builder().build().inject(this);
  }

  @Test
  public void scheduleSingle_oneExecution() throws Exception {
    setupTaskBarrier(2);

    scheduler.schedule(task, SLEEP_TIME);
    barrier.await();
    assertEquals(1, counter.get());
  }

  @Test
  public void scheduleSingle_twoExecutions() throws Exception {
    setupTaskBarrier(2);

    scheduler.schedule(task, SLEEP_TIME);
    for (int i = 0; i < ITERATIONS; i++) {
      barrier.await();
      assertEquals(i + 1, counter.get());
    }
  }

  @Test
  public void cancelSingle() throws Exception {
    setupTaskBarrier(2);

    scheduler.schedule(task, SLEEP_TIME);
    scheduler.cancel();
    barrier.await();

    assertEquals(1, counter.get());
  }

  private void setupTaskBarrier(int count) {
    counter = new AtomicInteger();
    barrier = new CyclicBarrier(count);
    task = () -> {
        counter.getAndIncrement();
        try {
          barrier.await();
        } catch (InterruptedException | BrokenBarrierException e) {
          throw new RuntimeException(e);
        }
      };
  }
}
