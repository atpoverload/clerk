package clerk.profilers;

import java.time.Instant;

/** Snapshot of the memory usage. */
public class MemoryStats {
  static final MemoryStats ZERO = new MemoryStats(null);

  private final Instant timestamp;
  private final long freeMemory;
  private final long totalMemory;

  MemoryStats() {
    timestamp = Instant.now();
    freeMemory = Runtime.getRuntime().freeMemory();
    totalMemory = Runtime.getRuntime().totalMemory();
  }

  private MemoryStats(Object nothing) {
    timestamp = Instant.EPOCH;
    freeMemory = 0;
    totalMemory = 0;
  }

  public long getFreeMemory() {
    return freeMemory;
  }

  public long getReservedMemory() {
    return totalMemory - freeMemory;
  }

  public long getTotalMemory() {
    return totalMemory;
  }

  @Override
  public String toString() {
    return Long.toString(getReservedMemory()) + "/" + Long.toString(getTotalMemory());
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof MemoryStats) {
      MemoryStats other = (MemoryStats) o;
      return this.timestamp.equals(Instant.EPOCH) && this.freeMemory == other.freeMemory && this.totalMemory == other.totalMemory;
    }
    return false;
  }

  @Override
  public int hashCode() {
    return 31 * (31 * (int) freeMemory + (int) totalMemory) + timestamp.hashCode();
  }
}
