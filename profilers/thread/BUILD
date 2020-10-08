load("@rules_java//java:defs.bzl", "java_library", "java_binary")
load("@dagger//:workspace_defs.bzl", "dagger_rules")

dagger_rules()

java_library(
  name = "thread_monitor",
  visibility = ["//visibility:public"],
  srcs = [
    "ThreadMonitor.java",
    "ThreadMonitoringModule.java",
  ],
  deps = [
    "//clerk",
    "//clerk/data:periodic_sampling",
    "//clerk/util:logger",
    ":dagger",
  ]
)

java_binary(
  name = "thread",
  visibility = ["//visibility:public"],
  main_class = "clerk.profilers.ThreadMonitor",
  runtime_deps = [
    ":thread_monitor",
  ]
)
