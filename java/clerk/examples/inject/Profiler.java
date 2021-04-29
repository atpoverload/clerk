package clerk.examples.inject;

import clerk.Clerk;
import dagger.Component;
import java.util.List;
import java.util.Map;

final class Profiler implements Clerk<Map<Class<?>, List<Object>>> {
  @Component(
    modules = {
      ClerkModule.class,
      DataProcessingModule.class,
      ExecutorModule.class,
      MemoryDataModule.class,
      TimeDataModule.class
    }
  )
  interface ClerkFactory {
    Clerk<Map<Class<?>, List<Object>>> newClerk();
  }

  private static final ClerkFactory factory = DaggerProfiler_ClerkFactory.builder().build();

  private final Clerk<Map<Class<?>, List<Object>>> clerk;

  Profiler() {
    clerk = factory.newClerk();
  }

  @Override
  public void start() {
    clerk.start();
  }

  @Override
  public void stop() {
    clerk.stop();
  }

  @Override
  public Map<Class<?>, List<Object>> read() {
    return clerk.read();
  }

  public static void main(String[] args) throws Exception {
    Profiler profiler = new Profiler();
    profiler.start();
    Thread.sleep(10000);
    profiler.stop();
    System.out.println(profiler.read());
  }
}
