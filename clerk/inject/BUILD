load("@rules_java//java:defs.bzl", "java_library")
load("@dagger//:workspace_defs.bzl", "dagger_rules")

dagger_rules()

java_library(
  name = "component",
  visibility = ["//visibility:public"],
  srcs = [
    "ClerkComponent.java",
  ],
  deps = [
    "//clerk",
    ":dagger",
  ]
)

java_library(
  name = "direct_sampling_module",
  visibility = ["//visibility:public"],
  srcs = ["DirectSamplingModule.java"],
  deps = [
    "//clerk",
    "//clerk/data",
    ":dagger",
  ],
)

java_library(
  name = "periodic_sampling_module",
  visibility = ["//visibility:public"],
  srcs = ["PeriodicSamplingModule.java"],
  deps = [
    ":component",
    "//clerk",
    "//clerk/data",
    ":dagger",
  ],
)

# java_library(
#   name = "clerk",
#   visibility = ["//visibility:public"],
#   srcs = [
#     "Clerk.java",
#   ],
#   deps = [
#     ":component",
#     "//clerk/util:logger",
#     ":dagger",
#   ],
#   exports = [
#     ":component",
#   ]
# )

java_library(
  name = "inject",
  visibility = ["//visibility:public"],
  srcs = [
    "ClerkModule.java",
  ],
  deps = [
    ":component",
    "//clerk",
    ":dagger",
  ],
  exports = [
    ":component",
  ]
)
