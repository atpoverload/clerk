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
    "//java/clerk",
    "//java/clerk:component",
    "//java/clerk/concurrent",
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
    "//java/clerk",
    "//java/clerk:component",
    "//java/clerk/concurrent",
    ":dagger",
  ],
)
