package clerk.examples.inject;

import clerk.DataProcessor;
import clerk.storage.ClassMappedListStorage;
import dagger.Module;
import dagger.Provides;
import java.util.List;
import java.util.Map;

@Module
interface DataProcessingModule {
  @Provides
  @ClerkComponent
  public static DataProcessor<?, Map<Class<?>, List<Object>>> provideProcessor() {
    return new ClassMappedListStorage<Object, Map<Class<?>, List<Object>>>() {
      @Override
      public Map<Class<?>, List<Object>> process() {
        return getData();
      }
    };
  }
}
