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

  /** Get the first value. */
  public final T getFirst() {
    return first;
  }

  /** Get the second value. */
  public final T getSecond() {
    return second;
  }

  /**
   * Returns the string representation of the values delimited by a comma and between parentheses.
   */
  @Override
  public String toString() {
    return "(" + first + "," + second + ")";
  }

  /** Compare if both pairs have the same values. */
  @Override
  public boolean equals(Object o) {
    if (o instanceof Pair) {
      Pair<T> other = (Pair<T>) o;
      return this.first.equals(other.first) && this.second.equals(other.second);
    }
    return false;
  }

  /** Return the combined hash of the values. */
  @Override
  public int hashCode() {
    return Objects.hash(first, second);
  }
}
