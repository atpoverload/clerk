package clerk.sampling;

import chappie.profiling.TimestampedSample;
import java.time.Instant;

/** Sample that wraps an Instant. */
public final class RuntimeSample implements TimestampedSample {
  private final Instant timestamp;

  TimestampedSample() {
    timestamp = Instant.now();
  }

  @Override
  public Instant getTimestamp() {
    return timestamp;
  }

  @Override
  public String toString() {
    return timestamp.getEpochMillis().toString();
  }
}
