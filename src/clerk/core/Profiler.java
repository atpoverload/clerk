package clerk.core;

import static java.util.logging.Level.WARNING;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

import clerk.utils.LoggerUtils;
import java.util.function.Supplier;
import java.util.logging.Logger;
import dagger.Lazy;
import javax.inject.Inject;

/** Manages a system that collects and processes data. */
public final class Profiler<I, O> {
  private static final Logger logger = LoggerUtils.setup();

  private final Iterable<Supplier<I>> sources;
  private final Lazy<DataProcessor<I, O>> processor;
  private final Lazy<Scheduler> scheduler;

  private boolean isRunning = false;

  @Inject
  Profiler(
    Iterable<Supplier<I>> sources,
    Lazy<DataProcessor<I, O>> processor,
    Lazy<Scheduler> scheduler) {
      this.sources = sources;
      this.processor = processor;
      this.scheduler = scheduler;
  }

  /**
   * Starts running the profiler, which feeds the output of the data sources
   * into the processor. All sources are sampled based on the scheduler.
   *
   * NOTE: the profiler will ignore this call if it is already running.
   */
  public void start() {
    if (!isRunning) {
      logger.fine("starting the profiler");
      for (Supplier<I> source: sources) {
        // is there a reason to use a listenable future?
        scheduler.get().schedule(() -> processor.get().add(source.get()));
        logger.fine("started sampling from " + source.getClass().getSimpleName());
      }
      // just in case there were no sources
      processor.get();
      isRunning = true;
      logger.fine("started the profiler");
    } else {
      logger.warning("profiler already running");
    }
  }

  /**
   * Stops running the profiler by canceling all scheduled tasks and dumping
   * the stored data.
   */
  public O stop() {
    if (isRunning) {
      logger.fine("stopping the profiler");
      scheduler.get().cancel();
      logger.fine("stopped the profiler");
      return processor.get().process();
    } else {
      logger.warning("profiler not currently running");
      return null;
    }
  }
}
