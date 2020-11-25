load("@rules_java//java:defs.bzl", "java_library")
load("@dagger//:workspace_defs.bzl", "dagger_rules")

dagger_rules()

java_library(
  name = "executor_module",
  visibility = ["//visibility:public"],
  srcs = [
    "ClerkExecutionModule.java",
  ],
  deps = [
    "//java/clerk:component",
    "//java/clerk:inject",
    "//java/clerk/data:policies",
    ":dagger",
  ],
)

java_library(
  name = "user_executor_module",
  visibility = ["//visibility:public"],
  srcs = [
    "ClerkUserExecutionModule.java",
  ],
  deps = [
    "//java/clerk:component",
    "//java/clerk:inject",
    "//java/clerk/data:policies",
    ":dagger",
  ],
)
