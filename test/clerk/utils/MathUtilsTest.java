package clerk.utils;

import static java.lang.Math.sqrt;
import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import org.junit.Test;

public class MathUtilsTest {

  @Test
  public void intArrayMean() throws Exception {
    int[] x = new int[] {1, 2, 3, 4, 5};
    double mean = MathUtils.mean(x);

    assertEquals(3, mean, 0);
  }

  @Test
  public void intListMean() throws Exception {
    List<Integer> x = List.of(1, 2, 3, 4, 5);
    double mean = MathUtils.mean(x);

    assertEquals(3, mean, 0);
  }

  @Test
  public void intStreamMean() throws Exception {
    IntStream x = IntStream.of(1, 2, 3, 4, 5);
    double mean = MathUtils.mean(x);

    assertEquals(3, mean, 0);
  }

  @Test
  public void longArrayMean() throws Exception {
    long[] x = new long[] {1, 2, 3, 4, 5};
    double mean = MathUtils.mean(x);

    assertEquals(3, mean, 0);
  }

  @Test
  public void longListMean() throws Exception {
    List<Long> x = List.of(1L, 2L, 3L, 4L, 5L);
    double mean = MathUtils.mean(x);

    assertEquals(3, mean, 0);
  }

  @Test
  public void longStreamMean() throws Exception {
    LongStream x = LongStream.of(1, 2, 3, 4, 5);
    double mean = MathUtils.mean(x);

    assertEquals(3, mean, 0);
  }

  @Test
  public void doubleArrayMean() throws Exception {
    double[] x = new double[] {1, 2, 3, 4, 5};
    double mean = MathUtils.mean(x);

    assertEquals(3, mean, 0);
  }

  @Test
  public void doubleListMean() throws Exception {
    List<Double> x = List.of(1.0, 2.0, 3.0, 4.0, 5.0);
    double mean = MathUtils.mean(x);

    assertEquals(3, mean, 0);
  }

  @Test
  public void doubleStreamMean() throws Exception {
    DoubleStream x = DoubleStream.of(1, 2, 3, 4, 5);
    double mean = MathUtils.mean(x);

    assertEquals(3, mean, 0);
  }

  @Test
  public void intArrayStd() throws Exception {
    int[] x = new int[] {1, 2, 3, 4, 5};
    double std = MathUtils.std(x);

    assertEquals(sqrt(2), std, 0);
  }

  @Test
  public void intListStd() throws Exception {
    List<Integer> x = List.of(1, 2, 3, 4, 5);
    double std = MathUtils.std(x);

    assertEquals(sqrt(2), std, 0);
  }

  @Test
  public void intStreamStd() throws Exception {
    IntStream x = IntStream.of(1, 2, 3, 4, 5);
    double std = MathUtils.std(x);

    assertEquals(sqrt(2), std, 0);
  }

  @Test
  public void longArrayStd() throws Exception {
    long[] x = new long[] {1, 2, 3, 4, 5};
    double std = MathUtils.std(x);

    assertEquals(sqrt(2), std, 0);
  }

  @Test
  public void longListStd() throws Exception {
    List<Long> x = List.of(1L, 2L, 3L, 4L, 5L);
    double std = MathUtils.std(x);

    assertEquals(sqrt(2), std, 0);
  }

  @Test
  public void longStreamStd() throws Exception {
    LongStream x = LongStream.of(1, 2, 3, 4, 5);
    double std = MathUtils.std(x);

    assertEquals(sqrt(2), std, 0);
  }

  @Test
  public void doubleArrayStd() throws Exception {
    double[] x = new double[] {1, 2, 3, 4, 5};
    double std = MathUtils.std(x);

    assertEquals(sqrt(2), std, 0);
  }

  @Test
  public void doubleListStd() throws Exception {
    List<Double> x = List.of(1.0, 2.0, 3.0, 4.0, 5.0);
    double std = MathUtils.std(x);

    assertEquals(sqrt(2), std, 0);
  }

  @Test
  public void doubleStreamStd() throws Exception {
    DoubleStream x = DoubleStream.of(1, 2, 3, 4, 5);
    double std = MathUtils.std(x);

    assertEquals(sqrt(2), std, 0);
  }
}
