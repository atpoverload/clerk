package clerk;

/** Interface for executing tasks. */
// TODO(timur): this needs a better naming convention
public interface ClerkExecutor {

  /** Run a workload. */
  void start(Runnable r);

  /** Stop all executor activity. */
  void stop();
}
