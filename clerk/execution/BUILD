load("@rules_java//java:defs.bzl", "java_library")

java_library(
  name = "execution",
  visibility = ["//visibility:public"],
  srcs = [
    "ExecutionPolicy.java",
  ],
)

java_library(
  name = "single",
  visibility = ["//visibility:public"],
  srcs = [
    "SingleExecutionPolicy.java",
  ],
  deps = [
    ":execution",
  ],
  exports = [
    ":execution"
  ]
)

java_library(
  name = "steady_state",
  visibility = ["//visibility:public"],
  srcs = [
    "SteadyStateExecutionPolicy.java",
  ],
  deps = [
    ":execution",
  ],
  exports = [
    ":execution"
  ]
)
