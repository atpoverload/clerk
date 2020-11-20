load("@rules_jmh//:defs.bzl", "jmh_java_benchmarks")

java_plugin(
  name = "_PeriodicClerkBenchmark_jmh_annotation_processor",
  deps = ["@rules_jmh_maven//:org_openjdk_jmh_jmh_generator_annprocess"],
  processor_class = "org.openjdk.jmh.generators.BenchmarkProcessor",
  visibility = ["//visibility:private"],
)

java_binary(
  name = "PeriodicClerkBenchmark",
  srcs = [
    "PeriodicClerkBenchmark.java",
  ],
  main_class = "clerk.benchmarks.PeriodicClerkBenchmark",
  deps = [
    "//java/clerk/jmh:profilers",
    "@rules_jmh_maven//:org_openjdk_jmh_jmh_core",
  ],
  plugins = [
    "_PeriodicClerkBenchmark_jmh_annotation_processor",
  ]
)
