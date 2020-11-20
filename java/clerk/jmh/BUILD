load("@rules_java//java:defs.bzl", "java_library")

java_library(
  name = "clerk",
  visibility = ["//visibility:public"],
  srcs = [
    "ClerkResult.java",
    "ClerkResultAggregator.java",
    "PeriodicJmhClerk.java",
  ],
  deps = [
    "//java/clerk:clerks",
    "//java/clerk/data",
    "@rules_jmh_maven//:org_openjdk_jmh_jmh_core",
  ],
)

java_library(
  name = "profilers",
  visibility = ["//visibility:public"],
  srcs = [
    "PeriodicClerkProfiler.java",
  ],
  deps = [
    ":clerk",
    "//java/clerk:clerks",
    "@rules_jmh_maven//:org_openjdk_jmh_jmh_core",
  ],
)
