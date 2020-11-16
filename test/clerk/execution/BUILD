load("@rules_java//java:defs.bzl", "java_test")

java_test(
  name = "SingleExecutionPolicyTest",
  srcs = ["SingleExecutionPolicyTest.java"],
  test_class = "clerk.execution.SingleExecutionPolicyTest",
  deps = [
    "//clerk/execution:single",
  ],
)


java_test(
  name = "SteadyStateExecutionPolicyTest",
  srcs = ["SteadyStateExecutionPolicyTest.java"],
  test_class = "clerk.execution.SteadyStateExecutionPolicyTest",
  deps = [
    "//clerk/execution:steady_state",
  ],
)
