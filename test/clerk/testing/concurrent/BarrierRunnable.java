package clerk.testing.concurrent;

import dagger.Component;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

public class BarrierRunnable implements Runnable {
  private static final AtomicInteger counter = new AtomicInteger();
  private static final CyclicBarrier barrier = new CyclicBarrier(2);

  public static void await() {
    try {
      barrier.await();
    } catch (InterruptedException | BrokenBarrierException e) {
      throw new RuntimeException(e);
    }
  }

  private final Runnable task;

  public BarrierRunnable(Runnable r) {
    this.task = () -> {
      try {
        r.run();
        barrier.await();
      } catch (InterruptedException | BrokenBarrierException e) {
        throw new RuntimeException(e);
      }
    };
  }

  @Override
  public void run() {
    task.run();
  }
}
