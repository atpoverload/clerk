package clerk;

import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

/**
 * A clerk that collects data on {@code start()} and {@code stop()}.
 *
 * <p>NOTE: Data operations are done on the calling thread.
 */
public class DirectClerk<O> implements Clerk<O> {
  private final Iterable<Supplier<?>> sources;
  private final Processor<?, O> processor;

  private boolean isRunning;

  public DirectClerk(Supplier<?> source, Processor<?, O> processor) {
    this.sources = List.of(source);
    this.processor = processor;
  }

  public DirectClerk(Collection<Supplier<?>> sources, Processor<?, O> processor) {
    this.sources = sources;
    this.processor = processor;
  }

  /** Pipes data into the processor. */
  @Override
  public final void start() {
    if (!isRunning) {
      isRunning = true;
      collectData();
    }
  }

  /** Pipes data into the processor. */
  @Override
  public final void stop() {
    if (isRunning) {
      collectData();
      isRunning = false;
    }
  }

  /** Return the processor's output. */
  @Override
  public final O read() {
    return processor.process();
  }

  /** Pipes data from the sources to the processor on the calling thread. */
  private void collectData() {
    for (Supplier<?> source : sources) {
      Clerk.pipe(source, processor);
    }
  }
}
