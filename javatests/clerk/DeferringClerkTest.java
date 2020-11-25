package clerk;

import static org.junit.Assert.assertEquals;

import clerk.testing.data.DummyStorage;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DeferringClerkTest {
  private AtomicInteger counter;
  private DeferringClerk<?> clerk;

  @Before
  public void setUp() {
    counter = new AtomicInteger();
    clerk = new DeferringClerk<Integer>(List.of(counter::incrementAndGet), new DummyStorage<>());
  }

  @After
  public void tearDown() {
    counter = null;
    clerk = null;
  }

  @Test
  public void read_noCalls() throws Exception {
    assertEquals(null, clerk.read().get());
  }

  @Test
  public void startRead_twoCalls() throws Exception {
    clerk.start();
    assertEquals(1, clerk.read().get());
  }

  @Test
  public void startStopRead_twoCalls() throws Exception {
    clerk.start();
    clerk.stop();
    assertEquals(2, clerk.read().get());
  }

  @Test
  public void startStartRead_twoCalls() throws Exception {
    clerk.start();
    clerk.start();
    assertEquals(1, clerk.read().get());
  }

  @Test
  public void stopRead_noCalls() throws Exception {
    clerk.stop();
    assertEquals(null, clerk.read().get());
  }
}
