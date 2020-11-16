load("@rules_java//java:defs.bzl", "java_library")
load("@dagger//:workspace_defs.bzl", "dagger_rules")

dagger_rules()

java_library(
  name = "steady_state",
  visibility = ["//visibility:public"],
  srcs = [
    "PeriodicSteadyStateExecutor.java",
  ],
  deps = [
    "//clerk:component",
    "//clerk/util",
    ":dagger",
  ],
)

java_library(
  name = "executor_module",
  visibility = ["//visibility:public"],
  srcs = [
    "ClerkExecutorModule.java",
  ],
  deps = [
    ":steady_state",
    "//clerk",
    "//clerk:component",
    ":dagger",
  ],
  exports = [
    ":steady_state",
  ]
)
