load("@rules_java//java:defs.bzl", "java_binary")

java_binary(
  name = "timeit",
  visibility = ["//visibility:public"],
  main_class = "clerk.contrib.Stopwatch",
  srcs = [
    "Stopwatch.java",
  ],
  deps = [
    "//clerk:sync",
    "//clerk/contrib/data",
    "//clerk/util:logger",
  ]
)

java_binary(
  name = "sizeit",
  visibility = ["//visibility:public"],
  main_class = "clerk.contrib.MemoryMonitor",
  srcs = [
    "MemoryMonitor.java",
  ],
  deps = [
    "//clerk:async",
    "//clerk/contrib/data",
    "//clerk/util:logger",
  ]
)
