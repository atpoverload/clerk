load("@rules_java//java:defs.bzl", "java_library")
load("@dagger//:workspace_defs.bzl", "dagger_rules")

dagger_rules()

java_library(
  name = "concurrent",
  visibility = ["//visibility:public"],
  srcs = [
    "Scheduler.java",
  ],
  deps = [
    ":dagger",
  ]
)

java_library(
  name = "direct_scheduler",
  visibility = [
    "//core/clerk:__subpackages__",
    "//profilers/clerk:__subpackages__",
  ],
  srcs = [
    "DirectScheduler.java",
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
    ":direct_scheduler",
    ":dagger",
  ],
  exports = [
    ":direct_scheduler",
  ]
)

java_library(
  name = "periodic_scheduler",
  visibility = [
    "//core/clerk:__subpackages__",
    "//profilers/clerk:__subpackages__",
  ],
  srcs = [
    "PeriodicExecutionScheduler.java",
    "PeriodicSchedulingRate.java",
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
    ":periodic_scheduler",
    ":dagger",
  ],
  exports = [
    ":periodic_scheduler",
  ]
)
