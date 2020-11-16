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

/** Utilities for general clerk needs. */
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

  /**
   * Casts the data source to the processor's input type and adds it.
   *
   * <p>If the input type is incorrect at runtime, the failure will be reported before re-throwing
   * the {@link ClassCastException}.
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
