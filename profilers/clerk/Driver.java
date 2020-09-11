package clerk;

import clerk.profilers.memory.MemoryMonitor;
// import clerk.profilers.timer.Timer;

public class Driver {
  public static void main(String[] args) throws Exception {
    MemoryMonitor.start();
    for (int i = 0; i < 10; i++) {
      Thread.sleep(1000);
      System.out.println(MemoryMonitor.dump());
    }
    MemoryMonitor.stop();

    System.out.println(MemoryMonitor.dump());

    System.out.println(MemoryMonitor.dump());
  }

  // public static void main(String[] args) throws Exception {
  //   Timer.start();
  //   for (int i = 0; i < 10; i++) {
  //     Thread.sleep(1000);
  //     System.out.println(Timer.dump());
  //   }
  //   Timer.stop();
  //
  //   System.out.println(Timer.dump());
  // }
}
