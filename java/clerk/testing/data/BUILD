load("@rules_java//java:defs.bzl", "java_library")
load("@dagger//:workspace_defs.bzl", "dagger_rules")

dagger_rules()

java_library(
  name = "dummy_storage",
  visibility = ["//visibility:public"],
  srcs = [
    "DummyStorage.java",
  ],
  deps = [
    "//java/clerk:processor",
  ],
)

java_library(
  name = "test_executor_module",
  visibility = ["//visibility:public"],
  srcs = [
    "TestExecutorModule.java",
  ],
  deps = [
    "//java/clerk",
    "//java/clerk:component",
    "//java/clerk/execution:steady_state",
    ":dagger",
  ],
)
