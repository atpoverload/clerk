package clerk.profilers.memory;

import clerk.Profiler;
import clerk.concurrent.DirectSamplingModule;
import dagger.Component;

public class MemoryMonitor {
  @Component(modules = {DirectSamplingModule.class, MemoryMonitorModule.class})
  interface ClerkFactory {
    Profiler<MemoryStats, Long> newClerk();
  }

  private static final ClerkFactory clerkFactory = DaggerMemoryMonitor_ClerkFactory.builder().build();

  private static Profiler clerk;

  // starts a profiler if there is not one
  public static void start() {
    if (clerk == null) {
      clerk = clerkFactory.newClerk();
      clerk.start();
    }
  }

  // stops the profiler if there is one
  public static long stop() {
    long profile = 0;
    
    if (clerk != null) {
      profile = (long) clerk.stop();
      clerk = null;
    }

    return profile;
  }
}
