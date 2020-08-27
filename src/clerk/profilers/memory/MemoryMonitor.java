package clerk.profilers.memory;

import clerk.concurrent.PeriodicSchedulingModule;
import clerk.core.Profiler;
import dagger.Component;
import java.time.Instant;
import java.util.Map;
import java.util.TreeMap;

public class MemoryMonitor {
  @Component(modules = {PeriodicSchedulingModule.class, MemoryMonitorModule.class})
  interface ClerkFactory {
    Profiler<MemoryStats, Map<Instant, MemoryStats>> newClerk();
  }

  private static final ClerkFactory clerkFactory = DaggerMemoryMonitor_ClerkFactory.builder().build();

  private static Profiler clerk;
  private static Map<Instant, MemoryStats> profile = new TreeMap<>();

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
      profile = (Map<Instant, MemoryStats>) clerk.stop();
      clerk = null;
    }
  }

  // restart the profiler so that we start fresh
  public static Map<Instant, MemoryStats> dump() {
    if (clerk != null) {
      stop();
      start();
    }
    return profile;
  }
}
