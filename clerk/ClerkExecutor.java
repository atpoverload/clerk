package clerk;

/** Interface for executing tasks. */
public interface ClerkExecutor {

  /** Run a workload. */
  void start(Runnable r);

  /** Stop all executor activity. */
  void stop();
}
