package clerk.core;

import static java.util.logging.Level.WARNING;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

import clerk.utils.LoggerUtils;
import java.util.ArrayList;
import java.util.Set;
import java.util.logging.Logger;
import java.util.concurrent.ExecutorService;
import javax.inject.Inject;

import clerk.concurrent.SteadyStateScheduledRunnable;
import clerk.sampling.SamplingRate;
import java.time.Duration;

/** Manages a system that collects profiles. */
public final class Profiler<T> {
  private static final Logger logger = LoggerUtils.setup();

  // this is implicitly scheduled
  private final Duration rate;
  private final ExecutorService executor;
  private final Set<Sampler> samplers;
  private final SampleProcessor<Iterable<T>> processor;

  private boolean isRunning = false;

  @Inject
  Profiler(
    @SamplingRate Duration rate,
    Set<Sampler> samplers,
    SampleProcessor<Iterable<T>> processor,
    ExecutorService executor) {
      this.rate = rate;
      this.samplers = samplers;
      this.processor = processor;
      this.executor = executor;
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
      logger.info("starting the profiler");
      for (Sampler sampler: samplers) {
        // is there a reason to use a listenable future?
        executor.execute(new SteadyStateScheduledRunnable(() -> {
          try {
            processor.add(sampler.sample());
          } catch (RuntimeException e) {
            logger.log(WARNING, "unable to sample", e);
            e.printStackTrace();
          }
        }, rate.toMillis()));
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
  public void stop() {
    if (isRunning) {
      logger.info("stopping the profiler");
      try {
        executor.shutdownNow();
        while (!executor.awaitTermination(250, MILLISECONDS)) {
          logger.fine("waiting for executor to terminate...");
        }
        isRunning = false;
      } catch (Exception e) {
        logger.log(WARNING, "unable to terminate executor", e);
      }
      logger.fine("stopped the profiler");
      getProfiles();
    } else {
      logger.warning("profiler not currently running");
    }
  }

  /** Processes the data and adds any profiles produced to the underlying storage. */
  public Iterable<T> getProfiles() {
    ArrayList<T> profiles = new ArrayList<>();
    int count = 0;
    for (T profile: processor.process()) {
      profiles.add(profile);
      count++;
    }
    if (count > 0) {
      logger.finest("collected " + count + " profiles from " + processor.getClass().getSimpleName());
    }
    return profiles;
  }
}
