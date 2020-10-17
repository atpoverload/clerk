package clerk.profilers;

import clerk.Clerk;
import clerk.data.PeriodicSamplingModule;
import clerk.util.ClerkLogger;
import dagger.Component;
import java.time.Instant;
import java.util.Arrays;
import java.util.Map;
import java.util.logging.Logger;

/** A profiler that measures elapsed time between calls as a {@link Instant}. */
public class ThreadMonitor {
  private static final Logger logger = ClerkLogger.createLogger();

  @Component(modules = {PeriodicSamplingModule.class, ThreadMonitoringModule.class})
  interface ClerkFactory {
    Clerk<Map<Instant, Integer>> newClerk();
  }

  private static final ClerkFactory clerkFactory =
      DaggerThreadMonitor_ClerkFactory.builder().build();

  private static Map<Instant, Integer> watch(Runnable r) {
    Clerk<Map<Instant, Integer>> clerk = clerkFactory.newClerk();
    clerk.start();
    r.run();
    clerk.stop();
    return clerk.dump();
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
      logger.info("command \"" + args[1] + "\" ran for " + watch(workload));
    }
  }
}
