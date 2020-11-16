load("@rules_java//java:defs.bzl", "java_test")
load("@dagger//:workspace_defs.bzl", "dagger_rules")

dagger_rules()

java_test(
  name = "ClerkTest",
  srcs = ["ClerkTest.java"],
  test_class = "clerk.ClerkTest",
  deps = [
    "//clerk",
    "//clerk/testing/data:test_data_module",
    "//clerk/testing/execution:test_executor_module",
    ":dagger",
  ],
)
