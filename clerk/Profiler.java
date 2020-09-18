package clerk;

public interface Profiler<O> {

  void start();

  O stop();
}
