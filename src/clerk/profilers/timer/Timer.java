package clerk.profilers.timer;

import clerk.core.Profiler;
import dagger.Component;
import java.time.Duration;

public class Timer {
  @Component(modules = {TimerModule.class})
  interface ClerkFactory {
    Profiler<Void, Duration> newClerk();
  }

  private static final ClerkFactory clerkFactory = DaggerTimer_ClerkFactory.builder().build();

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
      profile = (Duration) clerk.stop();
      clerk = null;
    }
  }

  // kill the profiler so that we start fresh
  public static Duration dump() {
    if (clerk != null) {
      stop();
      start();
    }
    return profile;
  }
}
