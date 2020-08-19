package clerk.utils;

import static java.lang.Math.sqrt;

import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/** Some math operations for collections. */
public final class MathUtils {
  // MEAN
  public static double mean(int[] X) {
    int sum = 0;
    for (int x: X) {
      sum += x;
    }
    return (double) sum / X.length;
  }

  public static double mean(long[] X) {
    long sum = 0;
    for (long x: X) {
      sum += x;
    }
    return (double) sum / X.length;
  }

  public static double mean(double[] X) {
    double sum = 0;
    for (double x: X) {
      sum += x;
    }
    return (double) sum / X.length;
  }

  public static <T extends Number> double mean(Iterable<T> X) {
    try {
      return mean(StreamSupport.stream(X.spliterator(), false).mapToInt(i -> (int) i));
    } catch (ClassCastException e) { }

    try {
      return mean(StreamSupport.stream(X.spliterator(), false).mapToLong(i -> (long) i));
    } catch (ClassCastException e) { }

    try {
      return mean(StreamSupport.stream(X.spliterator(), false).mapToDouble(i -> (double) i));
    } catch (ClassCastException e) { }

    return Double.NaN;
  }

  public static double mean(IntStream X) {
    return mean(X.toArray());
  }

  public static double mean(LongStream X) {
    return mean(X.toArray());
  }

  public static double mean(DoubleStream X) {
    return mean(X.toArray());
  }

  // STANDARD DEVIATION
  public static double std(int[] X) {
    double mean = mean(X);
    double variance = 0;
    for (int x: X) {
      double v = x - mean;
      variance += v * v;
    }
    return sqrt((double) variance / X.length);
  }

  public static double std(long[] X) {
    double mean = mean(X);
    double variance = 0;
    for (long x: X) {
      double v = x - mean;
      variance += v * v;
    }
    return sqrt((double) variance / X.length);
  }

  public static double std(double[] X) {
    double mean = mean(X);
    double variance = 0;
    for (double x: X) {
      double v = x - mean;
      variance += v * v;
    }
    return sqrt((double) variance / X.length);
  }

  public static <T extends Number> double std(Iterable<T> X) {
    try {
      return std(StreamSupport.stream(X.spliterator(), false).mapToInt(i -> (int) i));
    } catch (ClassCastException e) { }

    try {
      return std(StreamSupport.stream(X.spliterator(), false).mapToLong(i -> (long) i));
    } catch (ClassCastException e) { }

    try {
      return std(StreamSupport.stream(X.spliterator(), false).mapToDouble(i -> (double) i));
    } catch (ClassCastException e) { }

    return Double.NaN;
  }

  public static double std(IntStream X) {
    return std(X.toArray());
  }

  public static double std(LongStream X) {
    return std(X.toArray());
  }

  public static double std(DoubleStream X) {
    return std(X.toArray());
  }

  private MathUtils() { }
}
