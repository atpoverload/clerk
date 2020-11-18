load("@rules_java//java:defs.bzl", "java_binary")

java_binary(
  name = "timeit",
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
  name = "sizeit",
  main_class = "clerk.examples.MemoryMonitor",
  srcs = [
    "MemoryMonitor.java",
    "MemorySnapshot.java",
  ],
  deps = [
    "@clerk//java/clerk:clerks",
    "@clerk//java/clerk/data",
    "@clerk//java/clerk/util",
  ]
)
