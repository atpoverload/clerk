package clerk;

/** Interface that consumes and produces data. */
public interface Processor<I, O> {
  /** Adds data to the processor. */
  void add(I i);

  /** Processes the data and returns the result. */
  O process();
}
