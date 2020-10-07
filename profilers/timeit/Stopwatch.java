package clerk.profilers;

import clerk.Clerk;
import clerk.data.DirectSamplingModule;
import clerk.util.ClerkLogger;
import dagger.Component;
import java.time.Duration;
import java.util.Arrays;
import java.util.logging.Logger;

/** A profiler that measures elapsed time between calls as a {@link Duration}. */
public class Stopwatch {
  private static final Logger logger = ClerkLogger.createLogger();

  @Component(modules = {DirectSamplingModule.class, StopwatchModule.class})
  interface ClerkFactory {
    Clerk<Duration> newClerk();
  }

  private static final ClerkFactory clerkFactory = DaggerStopwatch_ClerkFactory.builder().build();

  private static Duration timeIt(Runnable r) {
    Clerk<Duration> timer = clerkFactory.newClerk();
    timer.start();
    r.run();
    timer.stop();
    return timer.dump();
  }

  public static void main(String[] args) throws Exception {
    if (args.length < 1) {

    } else {
      Runnable workload =
          () -> {
            try {
              String[] command =
                  args.length == 2 ? args[1].split(" ") : Arrays.copyOfRange(args, 1, args.length);
              new ProcessBuilder(command).start().waitFor();
            } catch (Exception e) {
              logger.info("unable to run command " + args[1]);
              e.printStackTrace();
            }
          };
      logger.info("command \"" + args[1] + "\" ran for " + timeIt(workload));
    }
  }
}
