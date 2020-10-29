load("@rules_java//java:defs.bzl", "java_binary")
load("@dagger//:workspace_defs.bzl", "dagger_rules")

dagger_rules()

java_binary(
  name = "timeit",
  visibility = ["//visibility:public"],
  main_class = "clerk.examples.inject.Stopwatch",
  srcs = [
    "Stopwatch.java",
    "StopwatchModule.java",
  ],
  deps = [
    "//clerk/examples/data",
    "//clerk/inject:component",
    "//clerk/inject:sync_module",
    "//clerk/util:logger",
    ":dagger"
  ]
)

java_binary(
  name = "sizeit",
  visibility = ["//visibility:public"],
  main_class = "clerk.examples.inject.MemoryMonitor",
  srcs = [
    "MemoryMonitor.java",
    "MemoryMonitorModule.java",
  ],
  deps = [
    "//clerk/examples:sizeit",
    "//clerk/examples/data",
    "//clerk/inject:component",
    "//clerk/inject:async_module",
    "//clerk/util:logger",
    ":dagger"
  ]
)
