package clerk;

import clerk.concurrent.ParallelExecutorModule;
import clerk.core.Profile;
import clerk.core.Profiler;
import clerk.sampling.SamplingRateModule;
import clerk.sampling.RuntimeSamplingModule;
import dagger.Component;
import java.time.Duration;
import java.util.ArrayList;

// temporary driver; should be able to get rid of this in some way or another
// some reading indicates that we would use a template to define these modules
// could look into an annotation for a profiler.
public class Clerk {
  @Component(modules = {
    ParallelExecutorModule.class,
    SamplingRateModule.class,
    RuntimeSamplingModule.class
  })

  interface ClerkFactory {
    Profiler<Duration> newClerk();
  }

  private static final ClerkFactory clerkFactory = DaggerClerk_ClerkFactory.builder().build();

  private static Profiler clerk;
  private static Duration profile = Duration.ZERO;

  // starts a profiler if there is not one
  public static void start() {
    if (clerk == null) {
      clerk = clerkFactory.newClerk();
      clerk.start();
    }
  }

  // stops the profiler if there is one
  public static void stop() {
    if (clerk != null) {
      clerk.stop();
      profile = (Duration) clerk.dump();
      clerk = null;
    }
  }

  // kill the profiler so that we start fresh
  public static Duration dump() {
    stop();
    start();
    return profile;
  }
}
