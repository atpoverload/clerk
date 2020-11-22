load("@rules_java//java:defs.bzl", "java_library")

java_library(
  name = "jmh",
  visibility = ["//visibility:public"],
  srcs = [
    "ClerkDummyProfiler.java",
    "TextResult.java",
  ],
  deps = [
    "//java/clerk:clerks",
    "//java/clerk/data",
    "//java/clerk/jmh:wrapper",
    "@rules_jmh_maven//:org_openjdk_jmh_jmh_core",
  ],
)
