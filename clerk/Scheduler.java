package clerk;

import java.time.Duration;
import java.time.Instant;
import java.util.function.Supplier;

public interface Scheduler {
  Duration getNextSchedulingTime(Supplier<?> source, Instant start);
}
