package clerk;

/** Interface for a data collection system. */
public interface Clerk<O> {
  /** Starts data collection. */
  void start();

  /** Stops data collection. */
  void stop();

  /** Returns the data. */
  O read();
}
