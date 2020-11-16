load("@rules_java//java:defs.bzl", "java_library")
load("@dagger//:workspace_defs.bzl", "dagger_rules")

dagger_rules()

java_library(
  name = "execution",
  visibility = ["//visibility:public"],
  srcs = [
    "ExecutionPolicy.java",
  ],
)

java_library(
  name = "settable",
  visibility = ["//visibility:public"],
  srcs = [
    "SettablePeriodicExecutionPolicy.java",
  ],
  deps = [
    ":execution",
  ],
  exports = [
    ":execution"
  ]
)

java_library(
  name = "single",
  visibility = ["//visibility:public"],
  srcs = [
    "SingleExecutionPolicy.java",
  ],
  deps = [
    ":execution",
  ],
  exports = [
    ":execution"
  ]
)

java_library(
  name = "steady_state",
  visibility = ["//visibility:public"],
  srcs = [
    "SteadyStatePeriodicExecutionPolicy.java",
  ],
  deps = [
    ":execution",
  ],
  exports = [
    ":execution"
  ]
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
)

java_library(
  name = "app_executor_module",
  visibility = ["//visibility:public"],
  srcs = [
    "ClerkApplicationExecutorModule.java",
  ],
  deps = [
    ":steady_state",
    "//clerk",
    "//clerk:component",
    ":dagger",
  ],
)
