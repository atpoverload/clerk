load("@rules_java//java:defs.bzl", "java_library")
load("@dagger//:workspace_defs.bzl", "dagger_rules")

dagger_rules()

java_library(
  name = "sync",
  visibility = ["//clerk:__subpackages__"],
  srcs = ["SynchronousExecutor.java"],
  deps = ["//clerk:component"],
  exports = ["//clerk:component"],
)

java_library(
  name = "direct_sampling",
  visibility = ["//visibility:public"],
  srcs = ["DirectSamplingModule.java"],
  deps = [
    ":sync",
    ":dagger",
  ],
)

java_library(
  name = "async",
  visibility = ["//clerk:__subpackages__"],
  srcs = ["AsynchronousSteadyStateExecutor.java"],
  deps = [
    "//clerk:component",
    ":dagger",
  ],
  exports = ["//clerk:component"],
)

java_library(
  name = "periodic_sampling",
  visibility = ["//visibility:public"],
  srcs = [
    "PeriodicSamplingModule.java",
  ],
  deps = [
    ":async",
    ":dagger",
  ],
  # exports = [
  #   ":async",
  # ]
)
