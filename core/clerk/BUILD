load("@rules_java//java:defs.bzl", "java_library")
load("@dagger//:workspace_defs.bzl", "dagger_rules")

dagger_rules()

java_library(
  name = "clerk",
  visibility = ["//visibility:public"],
  srcs = [
    "Processor.java",
  ],
)

java_library(
  name = "logger",
  srcs = [
    "ClerkLogger.java",
  ],
)

java_library(
  name = "profiler",
  visibility = ["//visibility:public"],
  srcs = [
    "Profiler.java",
  ],
  deps = [
    ":clerk",
    ":logger",
    "//core/clerk/concurrent",
    ":dagger",
  ],
)
