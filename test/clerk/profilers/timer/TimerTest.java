package clerk.profilers.timer;

import static java.lang.Math.abs;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TimerTest {

  private static final long THRESHOLD = 5;

  @Test
  public void test() throws Exception {
    long sleepTime = 1000;
    Timer.start();
    Thread.sleep(sleepTime);
    Timer.stop();

    assertTrue((Timer.dump().toMillis() - sleepTime) <= THRESHOLD);
  }
}
