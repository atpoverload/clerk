load("@rules_java//java:defs.bzl", "java_test")

java_test(
  name = "PairCollectionPolicyTest",
  srcs = ["PairCollectionPolicyTest.java"],
  test_class = "clerk.data.PairCollectionPolicyTest",
  deps = [
    "//java/clerk/data:policy",
    "//java/clerk/data:storage",
    "//java/clerk/testing/data",
  ],
)

java_test(
  name = "SingleCollectionPolicyTest",
  srcs = ["SingleCollectionPolicyTest.java"],
  test_class = "clerk.data.SingleCollectionPolicyTest",
  deps = [
    "//java/clerk/data:policy",
    "//java/clerk/testing/data",
  ],
)

java_test(
  name = "PairStorageTest",
  srcs = ["PairStorageTest.java"],
  test_class = "clerk.data.PairStorageTest",
  deps = [
    "//java/clerk/data:storage",
  ],
)

java_test(
  name = "ReturnableListStorageTest",
  srcs = ["ReturnableListStorageTest.java"],
  test_class = "clerk.data.ReturnableListStorageTest",
  deps = [
    "//java/clerk/data:storage",
  ],
)
