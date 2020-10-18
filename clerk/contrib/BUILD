load("@rules_java//java:defs.bzl", "java_binary")

java_binary(
  name = "timeit",
  visibility = ["//visibility:public"],
  main_class = "clerk.contrib.Stopwatch",
  srcs = [
    "Stopwatch.java",
  ],
  deps = [
    "//clerk",
    "//clerk/data",
    "//clerk/util:logger",
  ]
)
