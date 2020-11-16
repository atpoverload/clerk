load("@rules_java//java:defs.bzl", "java_test")

java_test(
  name = "DirectClerkTest",
  srcs = ["DirectClerkTest.java"],
  test_class = "clerk.clerks.DirectClerkTest",
  deps = [
    "//clerk/clerks",
    "//clerk/data",
  ],
)

java_test(
  name = "DeferredClerkTest",
  srcs = ["DeferredClerkTest.java"],
  test_class = "clerk.clerks.DeferredClerkTest",
  deps = [
    "//clerk/clerks",
    "//clerk/data",
  ],
)
