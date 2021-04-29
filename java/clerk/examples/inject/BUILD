load("@rules_java//java:defs.bzl", "java_binary", "java_library")
load("@dagger//:workspace_defs.bzl", "dagger_rules")

dagger_rules()

java_library(
  name = "inject",
  visibility = ["//visibility:public"],
  srcs = glob(["*.java"]),
  deps = [
    "//java/clerk",
    "//java/clerk/collectors",
    "//java/clerk/storage",
    "//java/clerk/util",
    ":dagger",
  ],
)

java_binary(
  name = "profiler",
  main_class = "clerk.examples.inject.Profiler",
  runtime_deps = [
    ":inject",
  ]
)
