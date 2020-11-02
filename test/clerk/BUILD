load("@rules_java//java:defs.bzl", "java_test")

java_test(
  name = "DirectClerkTest",
  srcs = ["DirectClerkTest.java"],
  test_class = "clerk.DirectClerkTest",
  deps = [
    "//clerk",
    "//clerk/examples/data",
  ],
)

java_test(
  name = "DeferredClerkTest",
  srcs = ["DeferredClerkTest.java"],
  test_class = "clerk.DeferredClerkTest",
  deps = [
    "//clerk",
    "//clerk/examples/data",
  ],
)
