load("@rules_java//java:defs.bzl", "java_test")

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
  name = "DeferringClerkTest",
  srcs = ["DeferringClerkTest.java"],
  test_class = "clerk.DeferringClerkTest",
  deps = [
    "//java/clerk:deferring",
    "//java/clerk/testing/data",
  ],
)
