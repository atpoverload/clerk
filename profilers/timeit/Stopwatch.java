package clerk.profilers;

import clerk.Clerk;
import clerk.Processor;
import clerk.data.SynchronousExecutor;
import clerk.util.ClerkLogger;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

/** A profiler that measures elapsed time between calls as a {@link Duration}. */
public class Stopwatch {
  private static final Logger logger = ClerkLogger.createLogger();

  private static Duration timeIt(Runnable r) {
    Clerk<Duration> clerk =
        new Clerk<>(List.of(Instant::now), new StopwatchTimer(), new SynchronousExecutor());
    clerk.start();
    r.run();
    clerk.stop();
    return clerk.dump();
  }

  private static class StopwatchTimer implements Processor<Instant, Duration> {
    private Instant start = Instant.EPOCH;
    private Instant end = Instant.EPOCH;

    @Override
    public void add(Instant timestamp) {
      if (start.equals(Instant.EPOCH)) {
        this.start = timestamp;
      } else if (end.equals(Instant.EPOCH)) {
        this.end = timestamp;
      } else {
        this.start = this.end;
        this.end = timestamp;
      }
    }

    @Override
    public Duration process() {
      return Duration.between(start, end);
    }
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
