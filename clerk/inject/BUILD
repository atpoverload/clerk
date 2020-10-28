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
    "//clerk",
    ":dagger",
  ]
)

java_library(
  name = "sync_module",
  visibility = ["//visibility:public"],
  srcs = [
    "SynchronousClerkModule.java",
  ],
  deps = [
    ":component",
    "//clerk:sync",
    ":dagger",
  ],
  exports = [
    "//clerk",
    "//clerk:sync",
  ]
)

java_library(
  name = "async_module",
  visibility = ["//visibility:public"],
  srcs = [
    "AsynchronousClerkModule.java",
  ],
  deps = [
    ":component",
    "//clerk:async",
    ":dagger",
  ],
  exports = [
    "//clerk",
    "//clerk:async",
  ]
)
