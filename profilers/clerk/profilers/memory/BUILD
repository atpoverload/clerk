load("@rules_java//java:defs.bzl", "java_library")
load("@dagger//:workspace_defs.bzl", "dagger_rules")

dagger_rules()

java_library(
  name = "memory",
  visibility = ["//visibility:public"],
  srcs = [
    "MemoryMonitor.java",
  ],
  deps = [
    ":module",
    "//core/clerk/concurrent:periodic_scheduling",
    "//core/clerk:profiler",
    ":dagger",
  ]
)

java_library(
  name = "module",
  visibility = ["//visibility:public"],
  srcs = [
    "MemoryMonitorModule.java",
    "MemoryStats.java",
  ],
  deps = [
    "//profilers/clerk/profilers",
    "//core/clerk",
    ":dagger",
  ]
)
