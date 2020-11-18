package clerk;

import static org.junit.Assert.assertEquals;

import clerk.testing.data.DummyStorage;
import java.time.Duration;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class PeriodicClerkTest {
  private AtomicInteger counter;
  private CyclicBarrier barrier;
  private PeriodicClerk<?> clerk;

  @Before
  public void setUp() {
    counter = new AtomicInteger();
    barrier = new CyclicBarrier(2);
    clerk =
        new PeriodicClerk<Integer>(
            () -> {
              awaitBarrier();
              return counter.incrementAndGet();
            },
            new DummyStorage<>(),
            Duration.ofMillis(0));
  }

  @After
  public void tearDown() {
    clerk.stop();
    counter = null;
    barrier = null;
    clerk = null;
  }

  @Test
  public void read_noCalls() throws Exception {
    assertEquals(null, clerk.read());
  }

  @Test
  public void stopRead_noCalls() throws Exception {
    clerk.stop();
    assertEquals(null, clerk.read());
  }

  @Test
  public void startRead_oneCall() throws Exception {
    clerk.start();
    awaitBarrier();
    assertEquals(1, clerk.read());
  }

  @Test
  public void startStopRead_oneCall() throws Exception {
    clerk.start();
    awaitBarrier();
    clerk.stop();
    assertEquals(1, clerk.read());
  }

  @Test
  public void startStartRead_oneCall() throws Exception {
    clerk.start();
    // clerk.start();
    awaitBarrier();
    assertEquals(1, clerk.read());
  }

  private void awaitBarrier() {
    try {
      barrier.await();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
