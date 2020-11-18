load("@rules_java//java:defs.bzl", "java_library")

java_library(
  name = "clerks",
  visibility = ["//visibility:public"],
  srcs = glob(["*.java"]),
  deps = [
    "//clerk:processor",
    "//clerk/util",
  ],
)
