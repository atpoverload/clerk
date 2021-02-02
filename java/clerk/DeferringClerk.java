package clerk;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.function.Supplier;

/**
 * A clerk that collects data on {@code start()} and {@code stop()}.
 *
 * <p>NOTE: Data operations are not done on the calling thread.
 */
public class DeferringClerk<O> implements Clerk<Future<O>> {
  private final Iterable<Supplier<?>> sources;
  private final Processor<?, O> processor;
  private final ExecutorService executor;
  private final ArrayList<Future<?>> dataFutures = new ArrayList<>();

  private boolean isRunning;

  public DeferringClerk(Supplier<?> source, Processor<?, O> processor, ExecutorService executor) {
    this.sources = List.of(source);
    this.processor = processor;
    this.executor = executor;
  }

  public DeferringClerk(
      Collection<Supplier<?>> sources, Processor<?, O> processor, ExecutorService executor) {
    this.sources = sources;
    this.processor = processor;
    this.executor = executor;
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
  public final Future<O> read() {
    return executor.submit(
        () -> {
          synchronized (dataFutures) {
            // make sure the previous futures are done or cancelled
            for (Future<?> future : dataFutures) {
              // attempt to cancel the future; if we can't, get the result safely
              if (!future.isDone() && !future.isCancelled() && !future.cancel(false)) {
                try {
                  future.get();
                } catch (Exception e) {
                  System.out.println("could not consume a data future");
                  e.printStackTrace();
                }
              }
            }
            dataFutures.clear();
            return processor.process();
          }
        });
  }

  /** Pipes data from the sources to the processor on the calling thread. */
  private void collectData() {
    synchronized (dataFutures) {
      for (Supplier<?> source : sources) {
        dataFutures.add(executor.submit(() -> Clerk.pipe(source, processor)));
      }
    }
  }
}
