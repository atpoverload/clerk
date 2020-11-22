package clerk.jmh;

import clerk.Clerk;
import java.util.Collection;
import org.openjdk.jmh.results.Result;

/** A {@link ClerkProfiler} that wraps a {@link Clerk}. */
public abstract class WrappedClerkProfiler extends ClerkProfiler {
  private final Clerk<Collection<? extends Result>> clerk;

  public WrappedClerkProfiler(Clerk<Collection<? extends Result>> clerk) {
    this.clerk = clerk;
  }

  /** Returns the underlying clerk used to collect data. */
  @Override
  protected Clerk<Collection<? extends Result>> getClerk() {
    return clerk;
  }
}
