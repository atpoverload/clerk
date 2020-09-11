package clerk.profilers.thread;

import clerk.concurrent.PeriodicSchedulingModule;
import clerk.core.Profiler;
import dagger.Component;
import java.time.Instant;
import java.util.Map;
import java.util.TreeMap;

public class ThreadTracker {
  @Component(modules = {PeriodicSchedulingModule.class, ThreadTrackerModule.class})
  interface ClerkFactory {
    Profiler<Integer, Map<Instant, Integer>> newClerk();
  }

  private static final ClerkFactory clerkFactory = DaggerThreadTracker_ClerkFactory.builder().build();

  private static Profiler clerk;
  private static Map<Instant, Integer> profile = new TreeMap<>();

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
      profile = (Map<Instant, Integer>) clerk.stop();
      clerk = null;
    }
  }

  // restart the profiler so that we start fresh
  public static Map<Instant, Integer> dump() {
    if (clerk != null) {
      stop();
      start();
    }
    return profile;
  }
}
