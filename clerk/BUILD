load("@rules_java//java:defs.bzl", "java_library")

java_library(
  name = "clerk",
  visibility = ["//visibility:public"],
  srcs = [
    "Clerk.java",
    "Processor.java",
  ],
  deps = [
    "//clerk/util:logger",
  ],
)

java_library(
  name = "sync",
  visibility = ["//visibility:public"],
  srcs = [
    "SynchronousClerk.java",
  ],
  deps = [
    ":clerk",
    "//clerk/util:logger",
  ],
  exports = [
    ":clerk",
  ]
)

java_library(
  name = "async",
  visibility = ["//visibility:public"],
  srcs = [
    "AsynchronousClerk.java",
  ],
  deps = [
    ":clerk",
    "//clerk/util:logger",
  ],
  exports = [
    ":clerk",
  ]
)
