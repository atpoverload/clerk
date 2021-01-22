package clerk;

import clerk.data.FixedPeriodPolicy;
import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Supplier;

/** A clerk that collects data at a fixed period. */
public class FixedPeriodClerk<O> implements Clerk<O> {
  private final Iterable<Supplier<?>> sources;
  private final Processor<?, O> processor;
  private final ScheduledExecutorService executor;
  private final FixedPeriodPolicy policy;

  private boolean isRunning = false;

  public FixedPeriodClerk(
      Supplier<?> source,
      Processor<?, O> processor,
      ScheduledExecutorService executor,
      Duration period) {
    this.sources = List.of(source);
    this.processor = processor;
    this.executor = executor;
    this.policy = new FixedPeriodPolicy(this.executor, period);
  }

  public FixedPeriodClerk(
      Collection<Supplier<?>> sources,
      Processor<?, O> processor,
      ScheduledExecutorService executor,
      Duration period) {
    this.sources = sources;
    this.processor = processor;
    // try to make a thread for each source
    // TODO(timurbey): should there be a limit?
    int workers = sources.size();
    this.executor = executor;
    this.policy = new FixedPeriodPolicy(this.executor, period);
  }

  /** Pipes data into the processor. */
  @Override
  public final void start() {
    if (!isRunning) {
      startCollecting();
      isRunning = true;
    }
  }

  /** Stops piping data into the processor. */
  @Override
  public final void stop() {
    if (isRunning) {
      policy.stop();
      isRunning = false;
    }
  }

  /** Return the processor's output. */
  @Override
  public final O read() {
    return processor.process();
  }

  private void startCollecting() {
    for (Supplier<?> source : sources) {
      policy.start(source, processor);
    }
  }
}
