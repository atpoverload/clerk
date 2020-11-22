load("@rules_java//java:defs.bzl", "java_library")

CORE_CLASSES = ["Clerk.java", "Processor.java"]

java_library(
  name = "clerk",
  visibility = ["//visibility:public"],
  srcs = CORE_CLASSES,
)

java_library(
  name = "abstract_clerk",
  visibility = ["//visibility:public"],
  srcs = [
    "AbstractClerk.java",
  ],
  deps = [
    ":clerk",
  ],
  exports = [
    ":clerk",
  ]
)

java_library(
  name = "clerks",
  visibility = ["//visibility:public"],
  srcs = [
    "DirectClerk.java",
    "PeriodicClerk.java"
  ],
  deps = [
    ":abstract_clerk",
    "//java/clerk/concurrent",
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
