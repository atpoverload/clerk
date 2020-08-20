load("@rules_java//java:defs.bzl", "java_test")
load("@dagger//:workspace_defs.bzl", "dagger_rules")

dagger_rules()

java_test(
    name = "SchedulerTest",
    srcs = ["SchedulerTest.java"],
    test_class = "clerk.concurrent.SchedulerTest",
    deps = [
        "//src/clerk/concurrent",
        "//src/clerk/concurrent:serial",
        ":dagger"
    ],
)
