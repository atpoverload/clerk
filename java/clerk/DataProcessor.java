package clerk;

/** Interface that consumes raw data and produces processed data. */
public interface DataProcessor<I, O> {
  /** Adds data to the processor. */
  void add(I i);

  /** Returns processed data. */
  O process();

  /** Error that should be used for failures in {@code process()}. */
  public class ProcessingError extends RuntimeException {
    public ProcessingError(Exception e) {
      super("unable to process data", e);
    }
  }
}
