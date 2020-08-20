load("@rules_java//java:defs.bzl", "java_library")
load("@dagger//:workspace_defs.bzl", "dagger_rules")

dagger_rules()

java_library(
  name = "parallel",
  visibility = ["//visibility:public"],
  srcs = [
    "ParallelSchedulingModule.java",
  ],
  deps = [
    ":concurrent",
    ":dagger",
  ]
)

java_library(
  name = "serial",
  visibility = ["//visibility:public"],
  srcs = [
    "SerialSchedulingModule.java",
  ],
  deps = [
    ":concurrent",
    ":dagger",
  ]
)

java_library(
  name = "concurrent",
  visibility = [
    "//src/clerk:__subpackages__",
    "//test/clerk:__subpackages__"
  ],
  srcs = [
    "Scheduler.java",
  ],
  deps = [
    ":dagger",
  ]
)
