package clerk.collectors;

import static org.junit.Assert.assertEquals;

import clerk.storage.SingleStorage;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class OnStopCollectorTest {
  private Supplier<Integer> source;
  private OnStopCollector collector;
  private SingleStorage<Integer, Integer> storage;

  @Before
  public void setUp() {
    AtomicInteger counter = new AtomicInteger();
    source = counter::getAndIncrement;
    collector = new OnStopCollector();
    storage =
        new SingleStorage<Integer, Integer>() {
          @Override
          public Integer process() {
            return getData();
          }
        };
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
    assertEquals(null, storage.process());
  }

  @Test
  public void collectStop() {
    collector.collect(source, storage);
    collector.stop();
    assertEquals(0, (int) storage.process());
  }

  @Test
  public void collectCollect() {
    collector.collect(source, storage);
    collector.collect(source, storage);
    assertEquals(null, storage.process());
  }

  @Test
  public void collectCollectStop() {
    collector.collect(source, storage);
    collector.collect(source, storage);
    collector.stop();
    assertEquals(1, (int) storage.process());
  }

  @Test
  public void stop() {
    collector.stop();
    assertEquals(null, storage.process());
  }
}
