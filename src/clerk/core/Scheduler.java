package clerk.core;

/** Interface for managing scheduled tasks. */
public interface Scheduler {

  /** Submits a new task to be scheduled. */
  void schedule(Runnable r);
  
  /** Stops all currently scheduled tasks. */
  void cancel();
}
