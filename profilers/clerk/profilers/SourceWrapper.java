package clerk.profilers;

import clerk.Processor;
import java.util.function.Supplier;

public final class SourceWrapper<O> extends Processor<O, O> {
  private Supplier<O> source;

  @Inject
  SourceWrapper(Iterable<Supplier<O>> sources) {
    for (Supplier<O> source: sources) {
      this.source = source;
      break;
    }
  }

  @Override
  public void accept(O o) { }

  @Override
  public O get() {
    return source.get();
  }
}
