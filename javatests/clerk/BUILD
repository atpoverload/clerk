load("@rules_java//java:defs.bzl", "java_test")
load("@dagger//:workspace_defs.bzl", "dagger_rules")

dagger_rules()

java_test(
  name = "DirectClerkTest",
  srcs = ["DirectClerkTest.java"],
  test_class = "clerk.DirectClerkTest",
  deps = [
    "//java/clerk:direct",
    "//java/clerk/testing/data",
  ],
)

java_test(
  name = "FixedPeriodClerkTest",
  srcs = ["FixedPeriodClerkTest.java"],
  test_class = "clerk.FixedPeriodClerkTest",
  deps = [
    "//java/clerk:fixed_period",
    "//java/clerk/testing/data",
  ],
)
