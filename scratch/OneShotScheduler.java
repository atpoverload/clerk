package clerk.concurrent;

import static java.util.concurrent.Executors.newSingleThreadExecutor;

import clerk.core.Scheduler;
import java.util.concurrent.ExecutorService;

/** Scheduler that runs tasks async when they are scheduled and cancelled. */
final class OneShotScheduler implements Scheduler {
  private final ArrayList<Runnable> tasks = new ArrayList<>();
  private final ExecutorService executor = newSingleThreadExecutor("clerk");

  /** Caches and runs the task. */
  @Override
  public void schedule(Runnable task) {
    tasks.add(task);
    executor.execute(task);
  }

  /** Runs all cahced tasks and clears the cache. */
  @Override
  public void cancel() {
    for (Runnable task: tasks) {
      executor.execute(task);
    }
    executor.shutdown();
  }
}
