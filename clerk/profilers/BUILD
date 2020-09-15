load("@rules_java//java:defs.bzl", "java_library", "java_binary")
load("@dagger//:workspace_defs.bzl", "dagger_rules")

dagger_rules()

java_library(
  name = "timer",
  visibility = ["//visibility:public"],
  srcs = [
    "Timer.java",
    "TimerModule.java",
  ],
  deps = [
    "//clerk",
    "//clerk:profiler",
    "//clerk/concurrent:direct_sampling",
    ":dagger",
  ]
)

java_library(
  name = "memory",
  visibility = ["//visibility:public"],
  srcs = [
    "MemoryMonitor.java",
    "MemoryMonitorModule.java",
    "MemoryStats.java",
  ],
  deps = [
    "//clerk",
    "//clerk:profiler",
    "//clerk/concurrent:direct_sampling",
    ":dagger",
  ]
)

java_library(
  name = "tracker",
  visibility = ["//visibility:public"],
  srcs = [
    "MemoryTracker.java",
    "MemoryTrackerModule.java",
    "MemoryStats.java",
  ],
  deps = [
    "//clerk",
    "//clerk:profiler",
    "//clerk/concurrent:periodic_sampling",
    ":dagger",
  ]
)

java_binary(
  name = "time_it",
  visibility = ["//visibility:public"],
  main_class = "clerk.profilers.Timer",
  runtime_deps = [
    ":timer",
  ]
)

java_binary(
  name = "size_it",
  visibility = ["//visibility:public"],
  main_class = "clerk.profilers.MemoryMonitor",
  runtime_deps = [
    ":memory",
  ]
)

java_binary(
  name = "track_it",
  visibility = ["//visibility:public"],
  main_class = "clerk.profilers.MemoryTracker",
  runtime_deps = [
    ":tracker",
  ]
)
