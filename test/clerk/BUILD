load("@rules_java//java:defs.bzl", "java_test")

java_test(
  name = "SynchronousClerkTest",
  srcs = ["SynchronousClerkTest.java"],
  test_class = "clerk.SynchronousClerkTest",
  deps = [
    "//clerk:sync",
    "//clerk/examples/data",
  ],
)

java_test(
  name = "AsynchronousClerkTest",
  srcs = ["AsynchronousClerkTest.java"],
  test_class = "clerk.AsynchronousClerkTest",
  deps = [
    "//clerk:async",
    "//clerk/examples/data",
  ],
)
