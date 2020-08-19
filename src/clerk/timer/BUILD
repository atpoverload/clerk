load("@dagger//:workspace_defs.bzl", "dagger_rules")
load("@rules_java//java:defs.bzl", "java_library")

dagger_rules()

java_library(
  name = "module",
  visibility = ["//visibility:public"],
  srcs = [
    "TimerModule.java",
  ],
  deps = [
    "//src/clerk/core",
    ":dagger",
  ]
)
