package clerk;

import static clerk.utils.MathUtils.mean;
import static clerk.utils.MathUtils.std;

import clerk.timer.Timer;
import clerk.utils.LoggerUtils;
import java.util.ArrayList;
import java.util.logging.Logger;
import java.time.Duration;

public class Driver {
  public static void main(String[] args) {
    ArrayList<Integer> profiles = new ArrayList<>();
    Logger logger = LoggerUtils.setup();
    logger.info("starting driver");
    // TODO(timur): need a real benchmark to work with
    for (int i = 0; i < 1000; i++) {
      Timer.start();
      try {
        Thread.sleep(5);
      } catch (Exception e) {
        e.printStackTrace();
      }
      Timer.stop();
      profiles.add((int) Timer.dump().toMillis());
    }
    logger.info("produced " + profiles.size() + " profiles:");
    logger.info("average sleep time: " + mean(profiles));
    logger.info("average sleep variance: " + std(profiles));
  }
}
