package clerk.profilers;

import clerk.Clerk;
import clerk.Profiler;
import clerk.concurrent.DirectSamplingModule;
import dagger.Component;
import java.io.File;
import java.time.Instant;
import java.time.Duration;
import java.util.ArrayList;

/** A profiler that measures elapsed time between calls as a {@link Duration}. */
public class Timer implements Profiler<Duration> {
  @Component(modules = {DirectSamplingModule.class, TimerModule.class})
  interface ClerkFactory {
    Clerk<Duration> newClerk();
  }

  private static final ClerkFactory clerkFactory = DaggerTimer_ClerkFactory.builder().build();

  private Clerk<Duration> clerk;

  // starts a profiler if there is not one
  public void start() {
    if (clerk == null) {
      clerk = clerkFactory.newClerk();
      clerk.start();
    }
  }

  // stops the profiler if there is one
  public Duration stop() {
    Duration profile = Duration.ZERO;

    if (clerk != null) {
      profile = clerk.stop();
      clerk = null;
    }

    return profile;
  }
}
