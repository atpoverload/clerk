load("@rules_java//java:defs.bzl", "java_binary")
load("@dagger//:workspace_defs.bzl", "dagger_rules")

dagger_rules()

java_binary(
  name = "timeit",
  visibility = ["//visibility:public"],
  main_class = "clerk.contrib.inject.Stopwatch",
  srcs = [
    "Stopwatch.java",
    "StopwatchModule.java",
  ],
  deps = [
    "//clerk",
    "//clerk/data",
    "//clerk/inject",
    "//clerk/inject:direct_sampling_module",
    "//clerk/util:logger",
    ":dagger"
  ]
)
