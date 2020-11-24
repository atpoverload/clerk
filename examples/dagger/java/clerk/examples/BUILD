load("@rules_java//java:defs.bzl", "java_binary")
load("@dagger//:workspace_defs.bzl", "dagger_rules")
dagger_rules()

java_binary(
  name = "memory_monitor",
  main_class = "clerk.examples.MemoryMonitor",
  srcs = [
    "MemoryMonitor.java",
    "MemoryMonitorModule.java",
    "MemorySnapshot.java",
  ],
  deps = [
    "@clerk//java/clerk",
    "@clerk//java/clerk:component",
    "@clerk//java/clerk:inject",
    "@clerk//java/clerk/data",
    "@clerk//java/clerk/dagger:executor_module",
    "@clerk//java/clerk/util",
    ":dagger",
  ]
)
