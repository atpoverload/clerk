load("@rules_java//java:defs.bzl", "java_library", "java_binary")

java_library(
  name = "monitor",
  visibility = ["//visibility:public"],
  srcs = [
    "MemoryMonitor.java",
  ],
  deps = [
    "//clerk",
    "//clerk/data",
    "//clerk/util:logger",
  ]
)

java_binary(
  name = "sizeit",
  visibility = ["//visibility:public"],
  main_class = "clerk.profilers.MemoryMonitor",
  runtime_deps = [
    ":monitor",
  ]
)
