load("@rules_java//java:defs.bzl", "java_library", "java_binary")
load("@dagger//:workspace_defs.bzl", "dagger_rules")

dagger_rules()

java_library(
  name = "stopwatch",
  visibility = ["//visibility:public"],
  srcs = [
    "Stopwatch.java",
    "StopwatchModule.java",
  ],
  deps = [
    "//clerk",
    "//clerk/data:direct_sampling",
    "//clerk/util:logger",
    ":dagger",
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
