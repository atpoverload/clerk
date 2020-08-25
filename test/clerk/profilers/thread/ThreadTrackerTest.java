package clerk.profilers.thread;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import java.time.Instant;
import org.junit.Test;

public class ThreadTrackerTest {

  private static final long THRESHOLD = 1;

  @Test
  public void test() throws Exception {
    long sleepTime = 1000;
    ThreadTracker.start();
    Thread.sleep(sleepTime);
    ThreadTracker.stop();

    assertTrue(ThreadTracker.dump().size() > 0);
  }
}
