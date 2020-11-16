package clerk.execution;

import java.util.concurrent.ScheduledExecutorService;

/** Interface that represents an execution policy. */
public interface ExecutionPolicy {
  public void execute(Runnable workload, ScheduledExecutorService executor);
}
