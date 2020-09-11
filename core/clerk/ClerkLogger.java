package clerk;

import java.util.Calendar;
import java.util.Date;
import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Logger;
import java.util.logging.LogRecord;
import java.text.SimpleDateFormat;

/** Logger to track the profiler events. */
final class ClerkLogger {
  private static boolean setup = false;

  public static Logger createLogger() {
    if (!setup) {
      try {
        // build the formatter and handlers
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss a z");
        Formatter formatter = new Formatter() {
          @Override
          public String format(LogRecord record) {
            String date = dateFormatter.format(new Date(record.getMillis()));
            return "clerk (" + date + ") [" + Thread.currentThread().getName() + "]: " + record.getMessage() + "\n";
          }
        };

        ConsoleHandler handler = new ConsoleHandler();
        handler.setFormatter(formatter);
        // TODO(timur): should add a file logger for transactions

        Logger logger = Logger.getLogger("clerk");
        logger.setUseParentHandlers(false);

        for (Handler hdlr: logger.getHandlers())
          logger.removeHandler(hdlr);

        logger.addHandler(handler);

        setup = true;
      } catch (Exception e) {
        setup = false;
      }
    }
    return Logger.getLogger("clerk");
  }

  private ClerkLogger() { }
}
