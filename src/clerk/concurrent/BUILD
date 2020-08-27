load("@rules_java//java:defs.bzl", "java_library")
load("@dagger//:workspace_defs.bzl", "dagger_rules")

dagger_rules()

java_library(
  name = "concurrent",
  visibility = ["//visibility:public"],
  srcs = [
    "PeriodicExecutionScheduler.java",
    "SchedulingRate.java",
  ],
  deps = [
    "//src/clerk/core",
    ":dagger",
  ]
)

java_library(
  name = "module",
  visibility = ["//visibility:public"],
  srcs = [
    "PeriodicSchedulingModule.java",
  ],
  deps = [
    ":concurrent",
    "//src/clerk/core",
    ":dagger",
  ]
)
