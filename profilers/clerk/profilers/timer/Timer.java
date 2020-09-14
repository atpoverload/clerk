package clerk.profilers.timer;

import clerk.Profiler;
import clerk.concurrent.DirectSamplingModule;
import dagger.Component;
import java.time.Instant;
import java.time.Duration;

public class Timer {
  @Component(modules = {DirectSamplingModule.class, TimerModule.class})
  interface ClerkFactory {
    Profiler<Instant, Duration> newClerk();
  }

  private static final ClerkFactory clerkFactory = DaggerTimer_ClerkFactory.builder().build();

  private static Profiler clerk;

  // starts a profiler if there is not one
  public static void start() {
    if (clerk == null) {
      clerk = clerkFactory.newClerk();
      clerk.start();
    }
  }

  // stops the profiler if there is one
  public static Duration stop() {
    Duration profile = Duration.ZERO;
    
    if (clerk != null) {
      profile = (Duration) clerk.stop();
      clerk = null;
    }

    return profile;
  }
}
