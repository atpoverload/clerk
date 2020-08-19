load("@dagger//:workspace_defs.bzl", "dagger_rules")
load("@rules_java//java:defs.bzl", "java_library")

dagger_rules()

java_library(
  name = "core",
  visibility = ["//visibility:public"],
  srcs = [
    "Sample.java",
    "SampleProcessor.java",
    "Sampler.java",
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
    "//src/clerk/concurrent",
    "//src/clerk/sampling:rate",
  ],
)
