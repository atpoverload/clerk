load("@rules_java//java:defs.bzl", "java_binary")

java_binary(
  name = "timeit",
  main_class = "clerk.examples.Stopwatch",
  srcs = [
    "Stopwatch.java",
  ],
  deps = [
    "//clerk",
    "//clerk/examples/data",
    "//clerk/util",
  ]
)
