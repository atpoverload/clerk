load("@rules_java//java:defs.bzl", "java_test")
load("@dagger//:workspace_defs.bzl", "dagger_rules")

dagger_rules()

java_test(
  name = "DirectClerkTest",
  srcs = ["DirectClerkTest.java"],
  test_class = "clerk.DirectClerkTest",
  deps = [
    "//java/clerk:clerks",
    "//java/clerk/data",
  ],
)

java_test(
  name = "FutureClerkTest",
  srcs = ["FutureClerkTest.java"],
  test_class = "clerk.FutureClerkTest",
  deps = [
    "//java/clerk:clerks",
    "//java/clerk/data",
  ],
)
