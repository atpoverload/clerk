load("@rules_java//java:defs.bzl", "java_library")

java_library(
  name = "clerks",
  visibility = ["//visibility:public"],
  srcs = glob(["*Clerk.java"], exclude = ["Clerk.java"]),
  deps = [
    "//java/clerk:processor",
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
  name = "processor",
  visibility = ["//visibility:public"],
  srcs = [
    "Processor.java",
  ],
)

java_library(
  name = "clerk",
  visibility = ["//visibility:public"],
  srcs = [
    "Clerk.java",
  ],
  deps = [
    ":component",
    "//java/clerk:processor",
    "//java/clerk/util",
    "@maven//:javax_inject_javax_inject",
  ],
)
