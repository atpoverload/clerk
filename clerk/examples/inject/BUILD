load("@rules_java//java:defs.bzl", "java_binary")
load("@dagger//:workspace_defs.bzl", "dagger_rules")

dagger_rules()

java_binary(
  name = "sizeit",
  visibility = ["//visibility:public"],
  main_class = "clerk.examples.inject.MemoryMonitor",
  srcs = [
    "MemoryMonitor.java",
    "MemoryMonitorModule.java",
    "MemorySnapshot.java",
  ],
  deps = [
    "//clerk:processor",
    "//clerk/examples/data",
    "//clerk/inject:component",
    "//clerk/inject:clerk",
    "//clerk/inject:executor_module",
    "//clerk/util",
    ":dagger"
  ]
)
