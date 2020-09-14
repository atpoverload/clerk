package clerk.concurrent;

/** Interface for executing tasks. */
public interface TaskRunner {

  /** Starts a new task. */
  void start(Runnable r);

  /** Stops all tasks. */
  void stop();
}
