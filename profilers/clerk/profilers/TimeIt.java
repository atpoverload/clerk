package clerk.profilers;

import java.io.File;

/** A profiler that measures elapsed time between calls as a {@link Duration}. */
public class TimeIt {
  public static void main(String[] args) throws Exception {
    // String pid = args[0];
    // File procPid = new File("/proc", args[0]);
    long sleepTime = Long.parseLong(args[0]);

    Timer timer = new Timer();

    for (int i = 0; i < 10; i++) {
      timer.start();

      // while (procPid.exists()) { }

      Thread.sleep(sleepTime);

      System.out.println("Ran for " + timer.stop());
    }
  }
}
