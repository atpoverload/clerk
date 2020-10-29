package clerk;

import static org.junit.Assert.assertEquals;

import clerk.examples.data.DummyStorage;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SynchronousClerkTest {
  private AtomicInteger counter;
  private SynchronousClerk<?> clerk;

  @Before
  public void setUp() {
    counter = new AtomicInteger();
    clerk = new SynchronousClerk<Integer>(List.of(counter::incrementAndGet), new DummyStorage<>());
  }

  @After
  public void tearDown() {
    counter = null;
    clerk = null;
  }

  @Test
  public void read_noCalls() {
    assertEquals(null, clerk.read());
  }

  @Test
  public void startRead_twoCalls() {
    clerk.start();
    assertEquals(2, clerk.read());
  }

  @Test
  public void startStopRead_twoCalls() {
    clerk.start();
    clerk.stop();
    assertEquals(2, clerk.read());
  }

  @Test
  public void startStartRead_twoCalls() {
    clerk.start();
    clerk.start();
    assertEquals(2, clerk.read());
  }

  @Test
  public void stopRead_noCalls() {
    clerk.stop();
    assertEquals(null, clerk.read());
  }
}
