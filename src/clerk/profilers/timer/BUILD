load("@rules_java//java:defs.bzl", "java_library")
load("@dagger//:workspace_defs.bzl", "dagger_rules")

dagger_rules()

java_library(
  name = "timer",
  visibility = ["//visibility:public"],
  srcs = [
    "Timer.java",
  ],
  deps = [
    ":module",
    "//src/clerk/core:profiler",
    ":dagger",
  ]
)

java_library(
  name = "module",
  visibility = ["//visibility:public"],
  srcs = [
    "TimerModule.java",
  ],
  deps = [
    "//src/clerk/core",
    ":dagger",
  ]
)
