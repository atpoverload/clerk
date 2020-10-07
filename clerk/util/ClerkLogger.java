package clerk.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/** Logger to track the events. */
public final class ClerkLogger {
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

  private static boolean setup = false;

  private static String logPrefix(Date date) {
    return "clerk (" + dateFormatter.format(date) + ") [" + Thread.currentThread().getName() + "]:";
  }

  public static Logger createLogger() {
    if (!setup) {
      try {
        // TODO(timur): add a file handler for transactions
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

  private ClerkLogger() {}
}
