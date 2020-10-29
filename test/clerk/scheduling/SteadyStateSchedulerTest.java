package clerk.scheduling;

import static org.junit.Assert.assertEquals;

import java.time.Duration;
import java.time.Instant;
import org.junit.Before;
import org.junit.Test;

public class SteadyStateSchedulerTest {
  private SteadyStateScheduler scheduler;

  @Before
  public void setUp() {
    scheduler = new SteadyStateScheduler(() -> Instant.ofEpochMilli(1), Duration.ofMillis(1));
  }

  @Test
  public void getNextSchedulingTime() {
    Instant timestamp = Instant.ofEpochMilli(0);
    assertEquals(0, scheduler.getNextSchedulingTime(null, timestamp).toMillis());
    assertEquals(0, scheduler.getNextSchedulingTime(() -> true, timestamp).toMillis());
    assertEquals(0, scheduler.getNextSchedulingTime(() -> 1, timestamp).toMillis());
    assertEquals(0, scheduler.getNextSchedulingTime(() -> 1.0, timestamp).toMillis());
    assertEquals(0, scheduler.getNextSchedulingTime(() -> "", timestamp).toMillis());
  }
}
