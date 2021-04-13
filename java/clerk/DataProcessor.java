package clerk;

/** Interface that consumes raw data and produces processed data. */
public interface DataProcessor<I, O> {
  /** Adds data to the processor. */
  void add(I i);

  /** Returns processed data. */
  O process();
}
