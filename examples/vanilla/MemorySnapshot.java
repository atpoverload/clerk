package clerk.examples;

import java.time.Instant;

/**
 * A class that provides a clerk that returns {@link MemorySnapshot}s from the most recent session.
 *
 * <p>{@code ListStorage} clears the underlying data, so that the data can only be consumed from the
 * {@link Clerk} once.
 */
final class MemorySnapshot {
  final Instant timestamp;
  final long totalMemory;
  final long freeMemory;

  MemorySnapshot(Instant timestamp, long totalMemory, long freeMemory) {
    this.timestamp = timestamp;
    this.totalMemory = totalMemory;
    this.freeMemory = freeMemory;
  }
}
