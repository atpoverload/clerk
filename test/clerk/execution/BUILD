load("@rules_java//java:defs.bzl", "java_test")

java_test(
  name = "SettablePeriodicExecutionPolicyTest",
  srcs = ["SettablePeriodicExecutionPolicyTest.java"],
  test_class = "clerk.execution.SettablePeriodicExecutionPolicyTest",
  deps = [
    "//clerk/execution:settable",
  ],
)

java_test(
  name = "SingleExecutionPolicyTest",
  srcs = ["SingleExecutionPolicyTest.java"],
  test_class = "clerk.execution.SingleExecutionPolicyTest",
  deps = [
    "//clerk/execution:single",
  ],
)


java_test(
  name = "SteadyStatePeriodicExecutionPolicyTest",
  srcs = ["SteadyStatePeriodicExecutionPolicyTest.java"],
  test_class = "clerk.execution.SteadyStatePeriodicExecutionPolicyTest",
  deps = [
    "//clerk/execution:steady_state",
  ],
)
