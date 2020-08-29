load("@rules_java//java:defs.bzl", "java_test")
load("@dagger//:workspace_defs.bzl", "dagger_rules")

dagger_rules()

java_test(
    name = "PeriodicExecutionSchedulerTest",
    srcs = ["PeriodicExecutionSchedulerTest.java"],
    test_class = "clerk.concurrent.PeriodicExecutionSchedulerTest",
    deps = [
        "//src/clerk/concurrent",
        "//src/clerk/core",
        "//src/clerk/testing/concurrent",
        "//src/clerk/testing/concurrent:module",
        ":dagger"
    ],
)
