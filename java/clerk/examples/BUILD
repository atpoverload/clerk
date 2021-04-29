load("@rules_java//java:defs.bzl", "java_binary", "java_library")

java_library(
  name = "examples",
  visibility = ["//visibility:public"],
  srcs = glob(["*.java"]),
  deps = [
    "//java/clerk/collectors",
    "//java/clerk/storage",
    "//java/clerk/util",
  ],
)

java_binary(
  name = "stopwatch",
  main_class = "clerk.examples.Stopwatch",
  runtime_deps = [":examples"]
)

java_binary(
  name = "burst_stopwatch",
  main_class = "clerk.examples.BurstStopwatch",
  runtime_deps = [":examples"]
)

java_binary(
  name = "memory_monitor",
  main_class = "clerk.examples.MemoryMonitor",
  runtime_deps = [":examples"]
)

java_binary(
  name = "profiler",
  main_class = "clerk.examples.Profiler",
  runtime_deps = [":examples"]
)
