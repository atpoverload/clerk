load("@rules_java//java:defs.bzl", "java_binary")

java_binary(
  name = "timeit",
  visibility = ["//visibility:public"],
  main_class = "clerk.examples.Stopwatch",
  srcs = [
    "Stopwatch.java",
  ],
  deps = [
    "//clerk:sync",
    "//clerk/examples/data",
    "//clerk/util:logger",
  ]
)

java_binary(
  name = "sizeit",
  visibility = ["//visibility:public"],
  main_class = "clerk.examples.MemoryMonitor",
  srcs = [
    "MemoryMonitor.java",
  ],
  deps = [
    "//clerk:async",
    "//clerk/examples/data",
    "//clerk/util:logger",
  ]
)
