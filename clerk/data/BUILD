load("@rules_java//java:defs.bzl", "java_binary")

java_library(
  name = "data",
  visibility = ["//visibility:public"],
  srcs = glob(["*.java"]),
  deps = [
    "//clerk:processor",
  ],
)
