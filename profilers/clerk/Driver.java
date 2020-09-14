package clerk;

import static java.util.concurrent.Executors.newScheduledThreadPool;

import clerk.profilers.memory.MemoryMonitor;
import java.util.concurrent.ExecutorService;

public class Driver {
  public static void main(String[] args) throws Exception {
    int length = 10000;
    int workers = 10;

    for (int i = 0; i < 10; i++) {
      MemoryMonitor.start();
      ExecutorService executor = newScheduledThreadPool(1);
      executor.execute(() -> {
        int[] vals = new int[length];
        for (int k = 0; k < vals.length; k++) {
          vals[k] = k;
        }
        System.out.println(Thread.currentThread().getName() + " reporting " + MemoryMonitor.stop() + " + reserved");
        MemoryMonitor.start();
      });

      executor.shutdown();
    }

    // Thread.sleep(250);

    // MemoryMonitor.start();
    // for (int i = 0; i < 10; i++) {
    //   int[] vals = new int[length];
    //   for (int k = 0; k < vals.length; k++) {
    //     vals[k] = i * k;
    //   }
    // }
    // Thread.sleep(250);
    // System.out.println(MemoryMonitor.stop());
  }
}
