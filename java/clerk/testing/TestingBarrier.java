package clerk.testing;

import java.util.concurrent.CyclicBarrier;

public final class TestingBarrier {
  private static final CyclicBarrier testBarrier = new CyclicBarrier(2);

  public static void awaitTestBarrier() {
    try {
      testBarrier.await();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static void resetTestBarrier() {
    while (testBarrier.getNumberWaiting() == 0) {}
    testBarrier.reset();
  }
}
