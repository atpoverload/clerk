package clerk.execution;

/** Execution policy that runs once on the calling thread. */
public final class SingleExecutionPolicy implements Executor {
  public SingleExecutionPolicy() {}

  @Override
  public void execute(Runnable r) {
    r.run();
  }
}
