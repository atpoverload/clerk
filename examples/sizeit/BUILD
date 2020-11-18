load("@rules_java//java:defs.bzl", "java_binary", "java_proto_library")
load("@rules_proto//proto:defs.bzl", "proto_library")

java_binary(
  name = "sizeit",
  main_class = "clerk.examples.MemoryMonitor",
  srcs = [
    "MemoryMonitor.java",
    "MemorySnapshot.java",
  ],
  deps = [
    "@clerk//clerk/clerks",
    "@clerk//clerk/data",
    "@clerk//clerk/util",
    "@com_google_protobuf//:protobuf_java",
  ]
)
