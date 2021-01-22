load("@rules_java//java:defs.bzl", "java_library")

java_library(
  name = "clerk",
  visibility = ["//visibility:public"],
  srcs = [
    "Clerk.java",
    "Processor.java",
    "CollectionPolicy.java",
  ],
)

java_library(
  name = "direct",
  visibility = ["//visibility:public"],
  srcs = [
    "DirectClerk.java",
  ],
  deps = [
    ":clerk",
  ],
)

java_library(
  name = "deferring",
  visibility = ["//visibility:public"],
  srcs = [
    "DeferringClerk.java",
  ],
  deps = [
    ":clerk",
  ],
)

java_library(
  name = "fixed_period",
  visibility = ["//visibility:public"],
  srcs = [
    "FixedPeriodClerk.java",
  ],
  deps = [
    ":clerk",
    "//java/clerk/data:policies",
  ],
)
