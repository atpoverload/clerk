package clerk;

public interface CollectionPolicy {
  /** Starts collecting using a given runnable. */
  // TODO(timurbey): should this be source + processor instead?
  public void start(Runnable r);

  /** Stops all collection. */
  public void stop();
}
