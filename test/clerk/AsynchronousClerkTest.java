package clerk;

import static java.util.concurrent.Executors.newSingleThreadScheduledExecutor;
import static org.junit.Assert.assertEquals;

import clerk.examples.data.DummyStorage;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class AsynchronousClerkTest {
  private AtomicBoolean locked;

  private AtomicInteger counter;
  private ScheduledExecutorService executor;
  private AsynchronousClerk<?> clerk;

  @Before
  public void setUp() {
    locked = new AtomicBoolean(false);
    counter = new AtomicInteger(0);
    executor = newSingleThreadScheduledExecutor();
    clerk =
        new AsynchronousClerk<Integer>(
            List.of(
                () -> {
                  locked.set(true);
                  while (!locked.get()) {}
                  return counter.incrementAndGet();
                }),
            new DummyStorage<>(),
            executor,
            Duration.ZERO);
  }

  @After
  public void tearDown() {
    executor.shutdown();
  }

  @Test
  public void read() {
    assertEquals(null, clerk.read());
  }

  @Test
  public void start() throws Exception {
    clerk.start();
    assertEquals(null, clerk.read());
    // TODO(timurbey): this is a crude barrier but is there a point in a real one?
    locked.set(false);
    while (!locked.get()) {
      Thread.sleep(1);
    }
    assertEquals(1, (int) clerk.read());
  }
}
