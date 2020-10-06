package clerk;

import clerk.concurrent.TaskRunner;
import java.util.function.Supplier;
import java.util.Set;
import javax.inject.Inject;

/** Manages a system that collects and processes data through a user API. */
public final class FutureClerk<O> {
  private final Iterable<Supplier<?>> sources;
  private final Processor<?, O> processor;
  private final ExecutorService executor;
  private final ArrayList<Future<?>> samplingFutures = new ArrayList<>();

  private boolean isRunning = false;

  @Inject
  Clerk(
    @DataSource Set<Supplier<?>> sources,
    Processor<?, O> processor,
    @DataSource ExecutorService executor) {
      this.sources = sources;
      this.processor = processor;
      this.executor = executor;
  }

  /**
   * Starts running the profiler, which feeds the output of the data sources
   * into the processor. All sources are sampled based on the sampler.
   *
   * NOTE: the profiler will ignore this call if it is already running.
   */
  public void start() {
    if (!isRunning) {
      for (Supplier<?> source: sources) {
        samplingFutures.add(executor.submit(() -> pipeData(source, processor)));
      }
      isRunning = true;
    }
  }

  /**
   * Stops running the profiler by canceling all scheduled tasks and dumping
   * the stored data.
   *
   * NOTE: the profiler will ignore this call if it is not running.
   */
  public Future<O> stop() {
    if (isRunning) {
      for (Future future: samplingFutures) {
        future.cancel(false);
      }
      return executor.submit(processor::get);
    }

    return null;
  }

  private <T> void pipeData(Supplier<?> source, Processor<T, ?> processor) {
    processor.accept((T) source.get());
  }
}
