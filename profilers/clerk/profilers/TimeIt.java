package clerk.profilers;

import java.io.File;

/** A profiler that measures elapsed time between calls as a {@link Duration}. */
public class TimeIt {
  public static void main(String[] args) throws Exception {
    String pid = args[0];
    File procPid = new File("/proc", args[0]);
    Timer timer = new Timer();
    timer.start();
    while (procPid.exists()) { }
    System.out.println("pid " + pid + " ran for " + timer.stop());
  }
}
