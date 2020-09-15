package clerk.profilers;

import clerk.Profiler;
import clerk.concurrent.DirectSamplingModule;
import dagger.Component;

/** A profiler that computes the difference in reserved memory between calls. */
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

  public static void main(String[] args) throws Exception {
    int iterations = 100;
    long memory = 0;

    Runnable workload = () -> {
      try {
        Thread.sleep(50);
      } catch (Exception e) {

      }};

    for (int i = 0; i < iterations; i++) {
      MemoryMonitor.start();
      workload.run();
      memory += MemoryMonitor.stop();
    }

    System.out.println("Workload " + workload.getClass().getSimpleName() + " consumed " + (memory / iterations) + " B");
  }
}
