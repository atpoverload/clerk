load("@rules_java//java:defs.bzl", "java_binary")

java_binary(
  name = "driver",
  visibility = ["//visibility:public"],
  main_class = "clerk.Driver",
  srcs = [
    "Driver.java"
  ],
  deps = [
    "//profilers/clerk/profilers/memory",
    # "//profilers/clerk/profilers/timer",
  ],
)
