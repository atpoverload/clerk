load("@dagger//:workspace_defs.bzl", "dagger_rules")
load("@rules_java//java:defs.bzl", "java_library")

dagger_rules()

java_library(
  name = "core",
  srcs = [
    "Profile.java",
    "Sample.java",
    "SampleProcessor.java",
    "Sampler.java",
  ],
)

java_library(
  name = "profiler",
  srcs = [
    "Profiler.java",
  ],
  deps = [
    ":core",
    ":dagger",
    "//src/clerk/utils",
  ],
)
