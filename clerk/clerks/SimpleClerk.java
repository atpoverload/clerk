package clerk.clerks;

/** Interface to represent a system that collects and processes data. */
public interface SimpleClerk<O> {
  /** Starts collecting data. */
  public void start();

  /** Stops collecting data. */
  public void stop();

  /** Consumes the collected data. */
  public O read();
}
