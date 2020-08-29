package clerk.concurrent;

import static org.junit.Assert.assertEquals;

import clerk.core.Scheduler;
import clerk.testing.concurrent.BarrierRunnable;
import clerk.testing.concurrent.TestSchedulingModule;
import dagger.Component;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicInteger;
import javax.inject.Inject;
import org.junit.Before;
import org.junit.Test;

public class PeriodicExecutionSchedulerTest {
  @Component(modules = {TestSchedulingModule.class})
  interface TestComponent {
    void inject(PeriodicExecutionSchedulerTest test);
  }

  private static final int ITERATIONS = 10;

  @Inject PeriodicExecutionScheduler scheduler;

  private AtomicInteger counter;
  private Runnable task;

  @Before
  public void setUp() {
    DaggerPeriodicExecutionSchedulerTest_TestComponent.builder().build().inject(this);
    counter = new AtomicInteger(0);
    task = new BarrierRunnable(() -> counter.incrementAndGet());
  }

  @Test
  public void scheduleSingle_oneExecution() throws Exception {
    scheduler.schedule(task);
    BarrierRunnable.await();

    assertEquals(1, counter.get());
  }

  // @Test
  // public void scheduleSingle_twoExecutions() throws Exception {
  //   scheduler.schedule(task);
  //   for (int i = 0; i < ITERATIONS; i++) {
  //     TestThread.await();
  //     assertEquals(i + 1, counter.get());
  //   }
  // }
  //
  // @Test
  // public void cancelSingle() throws Exception {
  //   scheduler.schedule(task);
  //   scheduler.cancel();
  //   TestThread.await();
  //
  //   assertEquals(1, counter.get());
  // }
}
