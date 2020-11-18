package clerk.util;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.NANOSECONDS;

import clerk.Processor;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Supplier;
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
  // TODO(timurbey): I've recently noticed this allows the wrong data types through in unit tests
  // but
  // not real code.
  public static <I> void pipe(Supplier<?> source, Processor<I, ?> processor) {
    try {
      Object data = source.get();
      processor.add((I) data);
    } catch (ClassCastException e) {
      Logger logger = getLogger();
      logger.severe("data source " + source.getClass() + " did not produce the expected type:");
      logger.severe(e.getMessage().split("\\(")[0]);
      throw e;
    }
  }

  public static void runAndReschedule(
      Runnable r, ScheduledExecutorService executor, Duration period) {
    Instant start = Instant.now();
    r.run();
    Duration rescheduleTime = period.minus(Duration.between(start, Instant.now()));

    if (rescheduleTime.toMillis() > 0) {
      executor.schedule(
          () -> runAndReschedule(r, executor, period), rescheduleTime.toMillis(), MILLISECONDS);
    } else if (rescheduleTime.toNanos() > 0) {
      executor.schedule(
          () -> runAndReschedule(r, executor, period), rescheduleTime.toNanos(), NANOSECONDS);
    } else {
      executor.execute(() -> runAndReschedule(r, executor, period));
    }
  }

  private ClerkUtil() {}
}
