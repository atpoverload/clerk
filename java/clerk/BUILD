load("@rules_java//java:defs.bzl", "java_library")

java_library(
  name = "clerk",
  visibility = ["//visibility:public"],
  srcs = [
    "Clerk.java",
    "Processor.java"
  ],
)

java_library(
  name = "direct",
  visibility = ["//visibility:public"],
  srcs = [
    "DirectClerk.java",
  ],
  deps = [
    ":clerk",
    "//java/clerk/util",
  ],
)

java_library(
  name = "fixed_period",
  visibility = ["//visibility:public"],
  srcs = [
    "FixedPeriodClerk.java"
  ],
  deps = [
    ":clerk",
    "//java/clerk/util",
  ],
)

java_library(
  name = "component",
  visibility = ["//visibility:public"],
  srcs = [
    "ClerkComponent.java",
  ],
  deps = [
    "@maven//:javax_inject_javax_inject",
  ]
)

java_library(
  name = "inject",
  visibility = ["//visibility:public"],
  srcs = [
    "InjectableClerk.java",
  ],
  deps = [
    ":clerk",
    ":component",
    "//java/clerk/util",
    "@maven//:javax_inject_javax_inject",
  ],
)
