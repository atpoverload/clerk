package clerk.sampling;

import clerk.core.Sample;
import java.time.Instant;

/** Interface for a timestamped sample. */
public interface TimestampedSample extends Sample {
  Instant getTimestamp();
}
