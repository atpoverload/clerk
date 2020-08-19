package clerk.core;

import static java.util.logging.Level.WARNING;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

import clerk.concurrent.Scheduler;
import clerk.utils.LoggerUtils;
import java.util.logging.Logger;
import javax.inject.Inject;

import clerk.sampling.SamplingRate;
import java.time.Duration;

/** Manages a system that collects profiles. */
public final class Profiler<T> {
  private static final Logger logger = LoggerUtils.setup();

  private final Iterable<Sampler> samplers;
  private final SampleProcessor<T> processor;

  // our execution is implicitly scheduled!
  private final Duration rate;
  private final Scheduler scheduler;

  private boolean isRunning = false;
  private T profile;

  @Inject
  Profiler(
    @SamplingRate Duration rate,
    Iterable<Sampler> samplers,
    SampleProcessor<T> processor,
    Scheduler scheduler) {
      this.rate = rate;
      this.samplers = samplers;
      this.processor = processor;
      this.scheduler = scheduler;
  }

  /**
   * Starts running the profiler, which feeds {@link sample()} of all samplers
   * into the processor. All samplers are scheduled to collect data at the same rate.
   *
   * NOTE: the profiler will ignore this call if it is already running.
   *
   * NOTE: all previously collected profiles are cleared and the processor is flushed
   *       when this is called.
   */
  public void start() {
    if (!isRunning) {
      logger.fine("starting the profiler");
      for (Sampler sampler: samplers) {
        // is there a reason to use a listenable future?
        scheduler.schedule(() -> {
          try {
            processor.add(sampler.sample());
          } catch (RuntimeException e) {
            logger.log(WARNING, "unable to sample", e);
            e.printStackTrace();
          }
        }, rate.toMillis());
        logger.fine("started " + sampler.getClass().getSimpleName());
      }
      isRunning = true;
      logger.fine("started the profiler");
    } else {
      logger.warning("profiler already running");
    }
  }

  /**
   * Starts running the profiler, which feeds {@link sample()} of all samplers
   * into the processor. All samplers are scheduled to collect data at the same rate.
   */
  public T stop() {
    if (isRunning) {
      logger.fine("stopping the profiler");
      scheduler.cancel();
      logger.fine("stopped the profiler");
      return dump();
    } else {
      logger.warning("profiler not currently running");
      return null;
    }
  }

  /** Processes the data and returns its output. */
  public T dump() {
    return processor.process();
  }
}
