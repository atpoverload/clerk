load("@dagger//:workspace_defs.bzl", "dagger_rules")
load("@rules_java//java:defs.bzl", "java_library")

dagger_rules()

java_library(
  name = "parallel_executor",
  visibility = ["//visibility:public"],
  srcs = [
    "ParallelExecutorModule.java",
  ],
  deps = [
    ":concurrent",
    "//src/clerk/core",
    "//src/clerk/sampling:rate",
    ":dagger",
  ]
)

java_library(
  name = "concurrent",
  visibility = ["//src/clerk:__subpackages__"],
  srcs = [
    "SteadyStateScheduledRunnable.java",
  ],
)
