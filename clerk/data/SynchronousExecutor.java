package clerk.data;

import clerk.ClerkExecutor;
import java.util.ArrayList;

/** Executor that executes workloads on start and stop. */
final class SynchronousExecutor implements ClerkExecutor {
  private final ArrayList<Runnable> tasks = new ArrayList<>();

  /** Stores a task and runs it. */
  @Override
  public void start(Runnable r) {
    tasks.add(r);
    r.run();
  }

  /** Runs all stored tasks and clears all tasks. */
  @Override
  public void stop() {
    for (Runnable r : tasks) {
      r.run();
    }
    tasks.clear();
  }
}