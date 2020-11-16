load("@rules_java//java:defs.bzl", "java_library")

java_library(
  name = "simple_clerk",
  visibility = ["//visibility:public"],
  srcs = [
    "SimpleClerk.java",
  ],
)

java_library(
  name = "clerks",
  visibility = ["//visibility:public"],
  srcs = [
    "DeferredClerk.java",
    "DirectClerk.java",
  ],
  deps = [
    ":simple_clerk",
    "//clerk:processor",
    "//clerk/util",
  ],
)
