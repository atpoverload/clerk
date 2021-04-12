package clerk.util;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DirectCollectorTest {
  private Supplier<Integer> source;
  private DirectCollector collector;
  private SingleStorage<Integer> storage;

  @Before
  public void setUp() {
    AtomicInteger counter = new AtomicInteger();
    source = counter::getAndIncrement;
    collector = new DirectCollector();
    storage = new SingleStorage<Integer>();
  }

  @After
  public void tearDown() {
    source = null;
    collector = null;
    storage = null;
  }

  @Test
  public void collect() {
    collector.collect(source, storage);
    assertEquals(0, (int) storage.process());
  }

  @Test
  public void collectStop() {
    collector.collect(source, storage);
    collector.stop();
    assertEquals(1, (int) storage.process());
  }

  @Test
  public void collectCollect() {
    collector.collect(source, storage);
    collector.collect(source, storage);
    assertEquals(1, (int) storage.process());
  }

  @Test
  public void collectCollectStop() {
    collector.collect(source, storage);
    collector.collect(source, storage);
    collector.stop();
    assertEquals(3, (int) storage.process());
  }

  @Test
  public void stop() {
    collector.stop();
    assertEquals(null, storage.process());
  }
}
