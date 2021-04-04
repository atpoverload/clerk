package clerk.testing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class PairTest {
  @Test
  public void equals_true() {
    assertEquals(new Pair<>("foo", "bar"), new Pair<>("foo", "bar"));
  }

  @Test
  public void equals_false() {
    assertNotEquals(new Pair<>("foo", "bar"), new Pair<>("foo", "baz"));
  }

  @Test
  public void hashCode_true() {
    assertEquals(new Pair<>("foo", "bar").hashCode(), new Pair<>("foo", "bar").hashCode());
  }

  @Test
  public void hashCode_false() {
    assertNotEquals(new Pair<>("foo", "bar").hashCode(), new Pair<>("foo", "baz").hashCode());
  }
}
