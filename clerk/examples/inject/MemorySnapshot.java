package clerk.examples.inject;

import java.time.Instant;

public class MemorySnapshot {
  public long memory;
  public Instant timestamp;

  MemorySnapshot(Instant timestamp, long memory) {
    this.timestamp = timestamp;
    this.memory = memory;
  }
}
