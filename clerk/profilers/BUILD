load("@rules_java//java:defs.bzl", "java_library", "java_binary")
load("@dagger//:workspace_defs.bzl", "dagger_rules")

dagger_rules()

java_library(
  name = "timer",
  visibility = ["//visibility:public"],
  srcs = [
    "Timer.java",
    "TimerModule.java",
  ],
  deps = [
    "//clerk",
    "//clerk:profiler",
    "//clerk/concurrent:direct_sampling",
    ":dagger",
  ]
)

java_binary(
  name = "time_it",
  visibility = ["//visibility:public"],
  main_class = "clerk.profilers.TimeIt",
  srcs = [
    "TimeIt.java",
  ],
  deps = [
    ":timer",
  ]
)
