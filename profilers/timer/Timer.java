package clerk.profilers;

import clerk.Clerk;
import clerk.data.DirectSamplingModule;
import clerk.util.ClerkLogger;
import dagger.Component;
import java.io.File;
import java.time.Duration;
import java.util.logging.Logger;

/** A profiler that measures elapsed time between calls as a {@link Duration}. */
public class Timer {
  private static final Logger logger = ClerkLogger.createLogger();

  @Component(modules = {DirectSamplingModule.class, TimerModule.class})
  interface ClerkFactory {
    Clerk<Duration> newClerk();
  }

  private static final ClerkFactory clerkFactory = DaggerTimer_ClerkFactory.builder().build();

  private static Duration timeIt(Runnable r) {
    Clerk<Duration> timer = clerkFactory.newClerk();
    timer.start();
    r.run();
    timer.stop();
    return timer.dump();
  }

  public static void main(String[] args) throws Exception {
    if (args[0].equals("--pid")) {
      File procPid = new File("/proc", args[1]);
      Runnable workload =
          () -> {
            while (procPid.exists()) {}
          };
      logger.info("pid " + args[1] + " ran for " + timeIt(workload));
    } else if (args[0].equals("-c")) {
      long sleepTime = Long.parseLong(args[1]);
      Runnable workload =
          () -> {
            try {
              Thread.sleep(sleepTime);
            } catch (Exception e) {

            }
          };
      logger.info("workload ran for " + timeIt(workload));
    }
  }
}
