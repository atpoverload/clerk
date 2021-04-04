package clerk.testing;

import java.util.Objects;

/** A class that stores two values of the same type. */
public class Pair<T> {
  private final T first;
  private final T second;

  public Pair(T first, T second) {
    this.first = first;
    this.second = second;
  }

  public final T getFirst() {
    return first;
  }

  public final T getSecond() {
    return second;
  }

  @Override
  public String toString() {
    return "(" + first + "," + second + ")";
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof Pair) {
      Pair<T> other = (Pair<T>) o;
      return this.first.equals(other.first) && this.second.equals(other.second);
    }
    return false;
  }

  @Override
  public int hashCode() {
    return Objects.hash(first, second);
  }
}
