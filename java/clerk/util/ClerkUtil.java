package clerk.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/** Utilities for general clerk needs. */
// TODO(timurbey): this does too many different things right now but I'm not sure how to deal with
// it
public final class ClerkUtil {
  private static final SimpleDateFormat dateFormatter =
      new SimpleDateFormat("yyyy-MM-dd HH:mm:ss a z");
  private static final Formatter formatter =
      new Formatter() {
        @Override
        public String format(LogRecord record) {
          return String.join(
              " ",
              logPrefix(new Date(record.getMillis())),
              record.getMessage(),
              System.lineSeparator());
        }
      };
  private static final AtomicInteger counter = new AtomicInteger();
  private static final ThreadFactory daemonThreadFactory =
      r -> {
        Thread t = new Thread(r, "clerk-" + counter.getAndIncrement());
        t.setDaemon(true);
        return t;
      };

  private static boolean setup = false;

  private static String logPrefix(Date date) {
    return String.join(
        " ",
        "clerk",
        "(" + dateFormatter.format(date) + ")",
        "[" + Thread.currentThread().getName() + "]:");
  }

  /** Sets up the logger, if necessary, and returns it. */
  // TODO(timurbey): there should be an injection to customize the logger
  public static synchronized Logger getLogger() {
    if (!setup) {
      try {
        // TODO(timurbey): add a file handler for transactions
        ConsoleHandler handler = new ConsoleHandler();
        handler.setFormatter(formatter);

        Logger logger = Logger.getLogger("clerk");
        logger.setUseParentHandlers(false);

        for (Handler hdlr : logger.getHandlers()) {
          logger.removeHandler(hdlr);
        }
        logger.addHandler(handler);

        setup = true;
      } catch (Exception e) {
        setup = false;
      }
    }
    return Logger.getLogger("clerk");
  }

  public static ThreadFactory daemonThreadFactory() {
    return daemonThreadFactory;
  }

  private ClerkUtil() {}
}
