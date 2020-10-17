load("@rules_java//java:defs.bzl", "java_library", "java_binary")

java_library(
  name = "stopwatch",
  visibility = ["//visibility:public"],
  srcs = [
    "Stopwatch.java",
  ],
  deps = [
    "//clerk",
    "//clerk/data",
    "//clerk/util:logger",
  ]
)

java_binary(
  name = "timeit",
  visibility = ["//visibility:public"],
  main_class = "clerk.profilers.Stopwatch",
  runtime_deps = [
    ":stopwatch",
  ]
)
