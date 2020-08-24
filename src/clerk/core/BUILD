load("@rules_java//java:defs.bzl", "java_library")
load("@dagger//:workspace_defs.bzl", "dagger_rules")

dagger_rules()

java_library(
  name = "core",
  visibility = ["//visibility:public"],
  srcs = [
    "DataProcessor.java",
    "Scheduler.java",
  ],
)

java_library(
  name = "profiler",
  visibility = ["//visibility:public"],
  srcs = [
    "Profiler.java",
  ],
  deps = [
    ":core",
    "//src/clerk/utils",
    ":dagger",
  ],
)
