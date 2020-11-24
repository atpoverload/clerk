load("@rules_java//java:defs.bzl", "java_library")

java_library(
  name = "sample",
  visibility = ["//visibility:public"],
  srcs = [
    "SampleProfiler.java",
    "SamplingResult.java",
  ],
  deps = [
    "@clerk//java/clerk:fixed_period",
    "@clerk//java/clerk/data",
    "@rules_jmh_maven//:org_openjdk_jmh_jmh_core",
  ],
)
