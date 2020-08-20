load("@rules_java//java:defs.bzl", "java_library")
load("@dagger//:workspace_defs.bzl", "dagger_rules")

dagger_rules()

java_library(
  name = "sampling",
  visibility = ["//visibility:public"],
  srcs = [
    "TimestampedSample.java",
  ],
  deps = [
    "//src/clerk/core",
  ]
)

java_library(
  name = "rate",
  visibility = ["//visibility:public"],
  srcs = [
    "SamplingRate.java",
  ],
  deps = [
    ":dagger",
  ]
)

java_library(
  name = "module",
  visibility = ["//visibility:public"],
  srcs = [
    "SamplingRateModule.java",
  ],
  deps = [
    ":rate",
    ":dagger",
  ]
)
