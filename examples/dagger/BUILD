load("@rules_java//java:defs.bzl", "java_binary", "java_proto_library")
load("@rules_proto//proto:defs.bzl", "proto_library")
load("@dagger//:workspace_defs.bzl", "dagger_rules")

dagger_rules()

java_binary(
  name = "sizeit",
  main_class = "clerk.examples.MemoryMonitor",
  srcs = [
    "MemoryMonitor.java",
    "MemoryMonitorModule.java",
    "MemorySnapshot.java",
  ],
  deps = [
    ":memory_snapshot_java_proto",
    "@clerk//clerk",
    "@clerk//clerk:component",
    "@clerk//clerk:processor",
    "@clerk//clerk/data",
    "@clerk//clerk/modules:executor_module",
    "@clerk//clerk/util",
    "@com_google_protobuf//:protobuf_java",
    ":dagger",
  ]
)

proto_library(
  name = "memory_snapshot_proto",
  srcs = ["memory_snapshot.proto"],
  deps = ["@com_google_protobuf//:timestamp_proto"],
)

java_proto_library(
  name = "memory_snapshot_java_proto",
  deps = [":memory_snapshot_proto"],
)
