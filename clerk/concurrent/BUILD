load("@rules_java//java:defs.bzl", "java_library")
load("@dagger//:workspace_defs.bzl", "dagger_rules")

dagger_rules()

java_library(
  name = "concurrent",
  visibility = ["//visibility:public"],
  srcs = [
    "TaskRunner.java",
  ],
)

java_library(
  name = "direct_task_runner",
  visibility = [
    "//clerk:__subpackages__",
  ],
  srcs = [
    "DirectTaskRunner.java",
  ],
  deps = [
    ":concurrent",
  ]
)

java_library(
  name = "direct_sampling",
  visibility = ["//visibility:public"],
  srcs = [
    "DirectSamplingModule.java",
  ],
  deps = [
    ":concurrent",
    ":direct_task_runner",
    ":dagger",
  ],
  exports = [
    ":direct_task_runner",
  ]
)

java_library(
  name = "periodic_task_runner",
  visibility = [
    "//clerk:__subpackages__",
  ],
  srcs = [
    "PeriodicTaskRunner.java",
    "SchedulingPeriod.java",
  ],
  deps = [
    ":concurrent",
    ":dagger",
  ]
)

java_library(
  name = "periodic_sampling",
  visibility = ["//visibility:public"],
  srcs = [
    "PeriodicSamplingModule.java",
  ],
  deps = [
    ":concurrent",
    ":periodic_task_runner",
    ":dagger",
  ],
  exports = [
    ":periodic_task_runner",
  ]
)
