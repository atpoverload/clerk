package clerk;

/** Interface for executing tasks. */
public interface Scheduler {

  /** Schedules a new task. */
  void schedule(Runnable r);

  /** Stops all tasks. */
  void cancel();
}
