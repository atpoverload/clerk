package clerk.util;

import clerk.Processor;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.function.Supplier;
import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/** Provides a logger that prints a prefix with the current timestamp and calling thread. */
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

  private static boolean setup = false;

  private static String logPrefix(Date date) {
    return String.join(
        " ",
        "clerk",
        "(" + dateFormatter.format(date) + ")",
        "[" + Thread.currentThread().getName() + "]:");
  }

  /** Helper method that sets up the logger before returning it, if necessary. */
  public static synchronized Logger getLogger() {
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

  /**
   * Helper method that casts the data source to the processor's input type.
   *
   * <p>If the input type is incorrect at runtime, then the failure will be reported before
   * abandoning the workload.
   */
  public static <I> void pipe(Supplier<?> source, Processor<I, ?> processor) {
    try {
      Object data = source.get();
      processor.add((I) data);
    } catch (ClassCastException e) {
      Logger logger = getLogger();
      logger.severe("data source " + source.getClass() + " was not the expected type:");
      logger.severe(e.getMessage().split("\\(")[0]);
      throw e;
    }
  }

  private ClerkUtil() {}
}
