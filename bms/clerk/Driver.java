package clerk;

import clerk.profilers.memory.MemoryMonitor;

public class Driver {
  public static void main(String[] args) throws Exception {
    MemoryMonitor.start();
    Thread.sleep(1000);
    MemoryMonitor.stop();
    System.out.println(MemoryMonitor.dump());
  }
}
