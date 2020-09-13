load("@rules_java//java:defs.bzl", "java_library")
load("@dagger//:workspace_defs.bzl", "dagger_rules")

dagger_rules()

java_library(
  name = "module",
  visibility = ["//visibility:public"],
  srcs = [
    "TestModule.java",
  ],
  deps = [
    "//core/clerk",
    ":dagger",
  ]
)
