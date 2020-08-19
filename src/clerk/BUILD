load("@rules_java//java:defs.bzl", "java_binary", "java_library")
load("@dagger//:workspace_defs.bzl", "dagger_rules")

dagger_rules()

java_library(
  name = "clerk",
  visibility = ["//visibility:public"],
  srcs = ["Clerk.java"],
  deps = [
    "//src/clerk/concurrent:module",
    "//src/clerk/core",
    "//src/clerk/core:profiler",
    "//src/clerk/sampling",
    "//src/clerk/sampling:module",
    "//src/clerk/timer:module",
    ":dagger",
  ],
)

java_binary(
  name = "driver",
  main_class = "clerk.Driver",
  srcs = ["Driver.java"],
  deps = [
    "clerk",
    "//src/clerk/utils",
  ],
)
