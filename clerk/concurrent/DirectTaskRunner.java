package clerk.concurrent;

import java.util.ArrayList;

/** Runner that executes all tasks when start and stop are called. */
final class DirectTaskRunner implements TaskRunner {
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
    for (Runnable r: tasks) {
      r.run();
    }
    tasks.clear();
  }
}
