load("@rules_java//java:defs.bzl", "java_library")

java_plugin(
    name = "clerk_example_jmh_annotation_processor",
    deps = ["@rules_jmh_maven//:org_openjdk_jmh_jmh_generator_annprocess"],
    processor_class = "org.openjdk.jmh.generators.BenchmarkProcessor",
    visibility = ["//visibility:private"],
)

java_binary(
    name = "benchmarks",
    srcs = glob(["*.java"]),
    main_class = "clerk.examples.SampleProfiler",
    deps = [
      "//java/clerk/examples:sample",
      "@rules_jmh_maven//:org_openjdk_jmh_jmh_core"],
    plugins = [":clerk_example_jmh_annotation_processor"],
)
