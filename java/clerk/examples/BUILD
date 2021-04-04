load("@rules_java//java:defs.bzl", "java_binary", "java_library")

java_library(
  name = "examples",
  visibility = ["//visibility:public"],
  srcs = glob(["*.java"]),
  deps = [
    "//java/clerk/util",
    "//java/clerk/util/concurrent",
  ],
)

java_binary(
  name = "stopwatch",
  main_class = "clerk.examples.Stopwatch",
  runtime_deps = [":examples"]
)

java_binary(
  name = "memory_monitor",
  main_class = "clerk.examples.MemoryMonitor",
  runtime_deps = [":examples"]
)
