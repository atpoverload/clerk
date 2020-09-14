load("@rules_java//java:defs.bzl", "java_library")
load("@dagger//:workspace_defs.bzl", "dagger_rules")

dagger_rules()

java_library(
  name = "logger",
  srcs = [
    "ClerkLogger.java",
  ],
)

java_library(
  name = "processor",
  visibility = ["//visibility:public"],
  srcs = [
    "DataProcessor.java",
  ],
)

java_library(
  name = "source",
  visibility = ["//visibility:public"],
  srcs = [
    "DataSource.java"
  ],
  deps = [
    ":dagger",
  ]
)

java_library(
  name = "profiler",
  visibility = ["//visibility:public"],
  srcs = [
    "Profiler.java",
  ],
  deps = [
    ":logger",
    ":processor",
    ":source",
    "//core/clerk/concurrent",
    ":dagger",
  ],
)
