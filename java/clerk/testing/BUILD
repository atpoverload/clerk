load("@rules_java//java:defs.bzl", "java_library")

java_library(
  name = "testing",
  visibility = ["//javatests:__subpackages__"],
  srcs = glob(["*.java"]),
  deps = [
    "//java/clerk",
  ],
)
