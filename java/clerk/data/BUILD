load("@rules_java//java:defs.bzl", "java_library")

java_library(
  name = "policy",
  visibility = ["//visibility:public"],
  srcs = [
    "ConcurrentCollectionPolicy.java",
    "FixedPeriodPolicy.java",
    "PairCollectionPolicy.java",
    "SingleCollectionPolicy.java",
  ],
  deps = [
    "//java/clerk",
  ]
)

java_library(
  name = "storage",
  visibility = ["//visibility:public"],
  srcs = [
    "AbstractListStorage.java",
    "AbstractPairStorage.java",
    "ReturnableListStorage.java",
  ],
  deps = [
    "//java/clerk",
  ],
)
