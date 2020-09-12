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
  name = "periodic_scheduling",
  visibility = ["//visibility:public"],
  srcs = [
    "PeriodicSchedulingModule.java",
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
