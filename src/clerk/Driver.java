package clerk;

import static clerk.utils.MathUtils.mean;
import static clerk.utils.MathUtils.std;

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
      Clerk.start();
      try {
        Thread.sleep(5);
      } catch (Exception e) {
        e.printStackTrace();
      }
      Clerk.stop();
      profiles.add((int) Clerk.dump().toMillis());
    }
    logger.info("produced " + profiles.size() + " profiles:");
    logger.info("average sleep time: " + mean(profiles));
    logger.info("average sleep variance: " + std(profiles));

    // print vs write profiles; work needs to be done here as well
    // for (Instant profile: Clerk.getProfiles()) {
    //   System.out.println(profile);
    // }

    // try (FileWriter fw = new FileWriter("chappie-logs/log.txt")) {
    //   PrintWriter writer = new PrintWriter(fw);
    //   writer.println("start,end,socket,total,attributed");
    //   for (Profile profile: Chappie.getProfiles()) {
    //     writer.println(profile.dump());
    //   }
    // } catch (Exception e) {
    //   logger.info("couldn't open log file");
    // }
  }
}
