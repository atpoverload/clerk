package clerk.execution;

import java.util.concurrent.ScheduledExecutorService;

/** Execution policy that runs a runnable once. */
public final class SingleExecutionPolicy implements ExecutionPolicy {
  public SingleExecutionPolicy() {}

  @Override
  public void execute(Runnable r, ScheduledExecutorService executor) {
    r.run();
  }
}
