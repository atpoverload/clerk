load("@rules_java//java:defs.bzl", "java_library")
load("@dagger//:workspace_defs.bzl", "dagger_rules")

dagger_rules()

java_library(
  name = "component",
  visibility = ["//visibility:public"],
  srcs = [
    "ClerkComponent.java",
    "ClerkExecutor.java",
    "Processor.java",
  ],
  deps = [
    ":dagger",
  ]
)

java_library(
  name = "clerk",
  visibility = ["//visibility:public"],
  srcs = [
    "Clerk.java",
  ],
  deps = [
    ":component",
    "//clerk/util:logger",
    ":dagger",
  ],
  exports = [
    ":component",
  ]
)
