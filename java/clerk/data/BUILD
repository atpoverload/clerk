load("@rules_java//java:defs.bzl", "java_library")

java_library(
  name = "policies",
  visibility = ["//visibility:public"],
  srcs = [
    "FixedPeriodPolicy.java",
    "SingleCollectionPolicy.java"
  ],
  deps = [
    "//java/clerk",
  ]
)

java_library(
  name = "processors",
  visibility = ["//visibility:public"],
  srcs = [
    "AbstractListStorage.java",
    "PairStorage.java",
    "ReturnableListStorage.java",
  ],
  deps = [
    "//java/clerk",
  ],
)
