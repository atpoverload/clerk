load("@rules_java//java:defs.bzl", "java_library")

java_plugin(
    name = "test_jmh_annotation_processor",
    deps = ["@org_openjdk_jmh_jmh_generator_annprocess"],
    processor_class = "org.openjdk.jmh.generators.BenchmarkProcessor",
    visibility = ["//visibility:private"],
)

java_binary(
  name = "jmh",
  visibility = ["//visibility:public"],
  main_class = "clerk.examples.jmh.ClerkProfiler",
  srcs = glob(["*.java"]),
  deps = [
    "//java/clerk",
    "//java/clerk/util",
    "//java/clerk/storage",
    "@org_openjdk_jmh_jmh_core",
  ],
  plugins = [":test_jmh_annotation_processor"]
)
