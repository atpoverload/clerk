load("@rules_java//java:defs.bzl", "java_library")
load("@dagger//:workspace_defs.bzl", "dagger_rules")

dagger_rules()

java_library(
  name = "module",
  visibility = ["//visibility:public"],
  srcs = [
    "TimerModule.java",
  ],
  deps = [
    "//core/clerk:processor",
    "//core/clerk:source",
    ":dagger",
  ]
)

java_library(
  name = "timer",
  visibility = ["//visibility:public"],
  srcs = [
    "Timer.java",
  ],
  deps = [
    ":module",
    "//core/clerk:profiler",
    "//core/clerk/concurrent:direct_sampling",
    ":dagger",
  ]
)
