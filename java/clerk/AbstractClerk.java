package clerk;

import java.util.concurrent.Executor;
import java.util.function.Supplier;

/** A clerk that collects data at a fixed period. */
public abstract class AbstractClerk<O> implements Clerk<O> {
  private final Iterable<Supplier<?>> sources;
  private final Processor<?, O> processor;

  protected boolean isRunning = false;

  protected AbstractClerk(Iterable<Supplier<?>> sources, Processor<?, O> processor) {
    this.sources = sources;
    this.processor = processor;
  }

  /** Get the data from the processor. */
  @Override
  public O read() {
    return processor.process();
  }

  /**
   * Pipes data from the sources to the processor on the calling thread.
   *
   * <p>Throws a RuntimeException if the clerk is not running.
   */
  protected final void pipeData() {
    if (!isRunning) {
      throw new RuntimeException("the clerk is not running");
    }
    for (Supplier<?> source : sources) {
      Clerk.pipe(source, processor);
    }
  }

  /**
   * Pipes data from the sources to the processor dispatched to an {@link Executor}.
   *
   * <p>Throws a RuntimeException if the clerk is not running.
   */
  protected final void pipeData(Executor executor) {
    for (Supplier<?> source : sources) {
      executor.execute(
          () -> {
            if (!isRunning) {
              throw new RuntimeException("the clerk is not running");
            }
            Clerk.pipe(source, processor);
          });
    }
  }
}
