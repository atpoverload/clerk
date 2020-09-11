load("@rules_java//java:defs.bzl", "java_library")
load("@dagger//:workspace_defs.bzl", "dagger_rules")

dagger_rules()

java_library(
  name = "thread",
  visibility = ["//visibility:public"],
  srcs = [
    "ThreadTracker.java",
  ],
  deps = [
    ":module",
    "//src/clerk/concurrent",
    "//src/clerk/concurrent:module",
    "//src/clerk/core:profiler",
    ":dagger",
  ]
)

java_library(
  name = "module",
  visibility = ["//visibility:public"],
  srcs = [
    "ThreadTrackerModule.java",
  ],
  deps = [
    "//src/clerk/core",
    ":dagger",
  ]
)
