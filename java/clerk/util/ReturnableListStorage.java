package clerk.util;

import java.util.List;

/** A processor that stores and returns data in a list. */
public final class ReturnableListStorage<I> extends ListStorage<I, List<I>> {
  /** Returns the data. */
  @Override
  public List<I> process() {
    return getData();
  }
}
