package clerk.profilers;

import clerk.Profiler;
import clerk.concurrent.PeriodicSamplingModule;
import dagger.Component;
import java.util.ArrayList;
import java.util.UUID;

/** A profiler that collects memory snapshots over the profiling interval. */
public class MemoryTracker {
  @Component(modules = {PeriodicSamplingModule.class, MemoryTrackerModule.class})
  interface ClerkFactory {
    Profiler<MemoryStats, Iterable<MemoryStats>> newClerk();
  }

  private static final ClerkFactory clerkFactory = DaggerMemoryTracker_ClerkFactory.builder().build();

  private static Profiler clerk;

  // starts a profiler if there is not one
  public static void start() {
    if (clerk == null) {
      clerk = clerkFactory.newClerk();
      clerk.start();
    }
  }

  // stops the profiler if there is one
  public static Iterable<MemoryStats> stop() {
    Iterable<MemoryStats> profile = new ArrayList<>();

    if (clerk != null) {
      profile = (Iterable<MemoryStats>) clerk.stop();
      clerk = null;
    }

    return profile;
  }

  public static void main(String[] args) throws Exception {
    int iterations = 100;
    long memory = 0;

    Runnable workload = () -> {
      ArrayList<UUID> list = new ArrayList<>();
      for (int i = 0; i < 100; i++) {
        list.add(UUID.randomUUID());
        try {
          Thread.sleep(1);
        } catch (Exception e) {

        }
      }
    };

    MemoryTracker.start();
    for (int i = 0; i < iterations; i++) {
      workload.run();
    }
    Iterable<MemoryStats> data = MemoryTracker.stop();

    for (MemoryStats stats: data) {
      System.out.println(stats);
    }
  }
}
