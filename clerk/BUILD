load("@rules_java//java:defs.bzl", "java_library")
load("@dagger//:workspace_defs.bzl", "dagger_rules")

dagger_rules()

java_library(
  name = "processor",
  visibility = ["//visibility:public"],
  srcs = [
    "Processor.java",
  ],
)

java_library(
  name = "simple_clerk",
  visibility = ["//visibility:public"],
  srcs = [
    "SimpleClerk.java",
  ],
)

java_library(
  name = "clerk",
  visibility = ["//visibility:public"],
  srcs = [
    "DeferredClerk.java",
    "DirectClerk.java",
  ],
  deps = [
    ":processor",
    ":simple_clerk",
    "//clerk/util",
  ],
)
