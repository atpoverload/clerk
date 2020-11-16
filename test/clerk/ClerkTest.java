package clerk;

import static clerk.testing.data.TestDataModule.awaitTestBarrier;
import static clerk.testing.data.TestDataModule.resetTestBarrier;
import static org.junit.Assert.assertEquals;

import clerk.testing.data.TestDataModule;
import clerk.testing.execution.TestExecutorModule;
import dagger.Component;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ClerkTest {
  @Component(modules = {TestDataModule.class, TestExecutorModule.class})
  interface ClerkFactory {
    Clerk<Integer> newClerk();
  }

  private static final ClerkFactory clerkFactory = DaggerClerkTest_ClerkFactory.builder().build();

  private Clerk<?> clerk;

  @Before
  public void setUp() {
    clerk = clerkFactory.newClerk();
  }

  @After
  public void tearDown() throws Exception {
    clerk = null;
  }

  @Test
  public void read_noCalls() throws Exception {
    assertEquals(null, clerk.read());
  }

  @Test
  public void stopRead_noCalls() throws Exception {
    clerk.stop();
    assertEquals(null, clerk.read());
  }

  @Test
  public void startRead_oneCall() throws Exception {
    clerk.start();
    awaitTestBarrier();
    assertEquals(1, clerk.read());

    clerk.stop();
    awaitTestBarrier();
    resetTestBarrier();
  }

  @Test
  public void startStopRead_oneCall() throws Exception {
    clerk.start();
    clerk.stop();
    awaitTestBarrier();
    assertEquals(1, clerk.read());

    awaitTestBarrier();
    resetTestBarrier();
  }

  // @Test
  // public void startStartRead_oneCall() throws Exception {
  //   clerk.start();
  //   clerk.start();
  //   awaitTestBarrier();
  //   assertEquals(1, clerk.read());
  //
  //   clerk.stop();
  //   awaitTestBarrier();
  //   resetTestBarrier();
  // }
}
