package clerk;

import clerk.concurrent.ParallelExecutorModule;
import clerk.core.Profile;
import clerk.core.Profiler;
import clerk.sampling.SamplingRateModule;
import clerk.sampling.RuntimeSamplingModule;
import dagger.Component;
import java.time.Instant;
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
    Profiler<Instant> newClerk();
  }

  private static final ClerkFactory clerkFactory = DaggerClerk_ClerkFactory.builder().build();

  private static Profiler clerk;
  private static ArrayList<Instant> profiles = new ArrayList<Instant>();

  // starts a profiler if there is not one already
  public static void start() {
    if (clerk == null) {
      profiles = new ArrayList<Instant>();
      clerk = clerkFactory.newClerk();
      clerk.start();
    }
  }

  // stops the profiler if there is one
  public static void stop() {
    if (clerk != null) {
      clerk.stop();
      profiles.addAll(clerk.getProfiles());
      clerk = null;
    }
  }

  // get all profiles stored
  public static Iterable<Instant> getProfiles() {
    stop();
    start();
    return profiles;
  }

  public static void clear() {
    profiles.clear();
  }
}
