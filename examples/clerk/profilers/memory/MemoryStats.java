package clerk.profilers.memory;

public class MemoryStats {
  private final long freeMemory;
  private final long totalMemory;

  MemoryStats() {
    freeMemory = Runtime.getRuntime().freeMemory();
    totalMemory = Runtime.getRuntime().totalMemory();
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
    return Long.toString(getReservedMemory());
  }
}
