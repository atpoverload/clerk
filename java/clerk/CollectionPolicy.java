package clerk;

import java.util.function.Supplier;

public interface CollectionPolicy {
  /** Starts collecting using a given source and processor. */
  public void start(Supplier<?> source, Processor<?, ?> processor);

  /** Stops all collection. */
  public void stop();
}
