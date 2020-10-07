package clerk.profilers;

import clerk.Clerk;
import clerk.data.DirectSamplingModule;
import dagger.Component;
import java.time.Duration;

/** A profiler that measures elapsed time between calls as a {@link Duration}. */
public class Timer { // implements Profiler<Duration> {
  @Component(modules = {DirectSamplingModule.class, TimerModule.class})
  interface ClerkFactory {
    Clerk<Duration> newClerk();
  }

  private static final ClerkFactory clerkFactory = DaggerTimer_ClerkFactory.builder().build();

  public static void main(String[] args) throws Exception {
    // let's do a config here?

    // String pid = args[0];
    // File procPid = new File("/proc", args[0]);
    // Runnable workload = () -> while (procPid.exists()) { }

    long sleepTime = Long.parseLong(args[0]);
    Runnable workload =
        () -> {
          try {
            Thread.sleep(sleepTime);
          } catch (Exception e) {

          }
        };

    Clerk<Duration> timer = clerkFactory.newClerk();
    for (int i = 0; i < 3; i++) {
      timer.start();
      workload.run();
      timer.stop();
      System.out.println("Ran for " + timer.dump());
    }
  }
}
