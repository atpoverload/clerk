package clerk.concurrent;

import java.util.ArrayList;

/** Scheduler that periodically runs tasks on an scheduled executor service. */
final class DirectScheduler implements Scheduler {
  private final ArrayList<Runnable> sources = new ArrayList<>();

  DirectScheduler() { }

  /** Starts a task that will be rescheduled. */
  @Override
  public void schedule(Runnable r) {
    sources.add(r);
    r.run();
  }

  /** Terminates all running threads. */
  @Override
  public void cancel() {
    for (Runnable r: sources) {
      r.run();
    }
    sources.clear();
  }
}
