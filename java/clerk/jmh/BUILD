load("@rules_java//java:defs.bzl", "java_library")

java_library(
  name = "jmh",
  visibility = ["//visibility:public"],
  srcs = [
    "ClerkProfiler.java",
  ],
  deps = [
    "//java/clerk",
    "@rules_jmh_maven//:org_openjdk_jmh_jmh_core",
  ],
)

java_library(
  name = "wrapper",
  visibility = ["//visibility:public"],
  srcs = [
    "WrappedClerkProfiler.java",
  ],
  deps = [
    ":jmh",
    "//java/clerk",
    "@rules_jmh_maven//:org_openjdk_jmh_jmh_core",
  ],
)
