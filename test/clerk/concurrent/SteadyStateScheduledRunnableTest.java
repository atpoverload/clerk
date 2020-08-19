package clerk.concurrent;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.atomic.AtomicInteger;
import org.junit.Test;

public class SteadyStateScheduledRunnableTest {

  @Test
  public void run() throws Exception {
    AtomicInteger i = new AtomicInteger();
    SteadyStateScheduledRunnable runnable = new SteadyStateScheduledRunnable(i::getAndIncrement, 1000);

    // bad
    Thread thread = new Thread(runnable);
    thread.start();
    thread.interrupt();
    thread.join();

    assertEquals(1, i.get());
  }
}
