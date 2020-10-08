load("@rules_java//java:defs.bzl", "java_library", "java_binary")
load("@dagger//:workspace_defs.bzl", "dagger_rules")

dagger_rules()

java_library(
  name = "memory_monitor",
  visibility = ["//visibility:public"],
  srcs = [
    "MemoryMonitor.java",
    "MemoryMonitoringModule.java",
  ],
  deps = [
    "//clerk",
    "//clerk/data:periodic_sampling",
    "//clerk/util:logger",
    ":dagger",
  ]
)

java_binary(
  name = "sizeit",
  visibility = ["//visibility:public"],
  main_class = "clerk.profilers.MemoryMonitor",
  runtime_deps = [
    ":memory_monitor",
  ]
)
