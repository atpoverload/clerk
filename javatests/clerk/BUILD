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

java_test(
  name = "PeriodicClerkTest",
  srcs = ["PeriodicClerkTest.java"],
  test_class = "clerk.PeriodicClerkTest",
  deps = [
    "//java/clerk:clerks",
    "//java/clerk/data",
    "//java/clerk/testing:barrier",
    "//java/clerk/testing/data:dummy_storage",
  ],
)

java_test(
  name = "ClerkTest",
  srcs = ["ClerkTest.java"],
  test_class = "clerk.ClerkTest",
  deps = [
    "//java/clerk",
    "//java/clerk/testing/data:test_data_module",
    "//java/clerk/testing/execution:test_executor_module",
    ":dagger",
  ],
)
