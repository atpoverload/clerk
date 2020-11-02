load("@rules_java//java:defs.bzl", "java_library")
load("@dagger//:workspace_defs.bzl", "dagger_rules")

dagger_rules()

java_library(
  name = "component",
  visibility = ["//visibility:public"],
  srcs = [
    "ClerkComponent.java",
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
    "//clerk:processor",
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
    ":component",
    ":clerk",
    ":dagger",
  ],
)
