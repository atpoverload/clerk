load("@rules_java//java:defs.bzl", "java_test")

java_test(
  name = "DirectCollectorTest",
  srcs = ["DirectCollectorTest.java"],
  test_class = "clerk.collectors.DirectCollectorTest",
  deps = [
    "//java/clerk/collectors",
    "//java/clerk/storage",
  ],
)

java_test(
  name = "OnStartCollectorTest",
  srcs = ["OnStartCollectorTest.java"],
  test_class = "clerk.collectors.OnStartCollectorTest",
  deps = [
    "//java/clerk/collectors",
    "//java/clerk/storage",
  ],
)

java_test(
  name = "OnStopCollectorTest",
  srcs = ["OnStopCollectorTest.java"],
  test_class = "clerk.collectors.OnStopCollectorTest",
  deps = [
    "//java/clerk/collectors",
    "//java/clerk/storage",
  ],
)
