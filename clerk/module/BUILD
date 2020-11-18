load("@rules_java//java:defs.bzl", "java_library")
load("@dagger//:workspace_defs.bzl", "dagger_rules")

dagger_rules()

java_library(
  name = "executor_module",
  visibility = ["//visibility:public"],
  srcs = [
    "ClerkExecutorModule.java",
  ],
  deps = [
    "//clerk",
    "//clerk:component",
    "//clerk/concurrent:steady_state",
    ":dagger",
  ],
)

java_library(
  name = "user_executor_module",
  visibility = ["//visibility:public"],
  srcs = [
    "ClerkUserExecutorModule.java",
  ],
  deps = [
    "//clerk",
    "//clerk:component",
    "//clerk/concurrent:steady_state",
    ":dagger",
  ],
)
