package clerk.data;

import java.util.List;

/** A processor that stores data in a list as added and pops the list when processed. */
public final class ReturnableListStorage<I> extends AbstractListStorage<I, List<I>> {
  public ReturnableListStorage() {}

  /** Returns the stored data. */
  @Override
  public final List<I> process() {
    return getData();
  }
}
