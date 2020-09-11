package clerk.concurrent;

import static java.util.concurrent.Executors.newSingleThreadExecutor;

import clerk.core.Scheduler;
import java.util.concurrent.ExecutorService;

/** Scheduler that runs tasks async when they are scheduled and cancelled. */
final class DirectScheduler implements Scheduler {
  /** Caches and runs the task. */
  @Override
  public void schedule(Runnable task) {
    task.run();
  }

  /** Runs all cached tasks and clears the cache. */
  @Override
  public void cancel() { }
}
