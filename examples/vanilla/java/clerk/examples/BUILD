load("@rules_java//java:defs.bzl", "java_binary")

java_binary(
  name = "stopwatch",
  main_class = "clerk.examples.Stopwatch",
  srcs = [
    "Stopwatch.java",
  ],
  deps = [
    "@clerk//java/clerk:clerks",
    "@clerk//java/clerk/data",
    "@clerk//java/clerk/util",
  ]
)

java_binary(
  name = "memory_monitor",
  main_class = "clerk.examples.MemoryMonitor",
  srcs = [
    "MemoryMonitor.java",
    "MemorySnapshot.java",
  ],
  deps = [
    "@clerk//java/clerk:fixed_period",
    "@clerk//java/clerk/data",
    "@clerk//java/clerk/util",
  ]
)
