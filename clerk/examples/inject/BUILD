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
    "//clerk/contrib/data",
    "//clerk/inject:component",
    "//clerk/inject:sync_module",
    "//clerk/util:logger",
    ":dagger"
  ]
)
