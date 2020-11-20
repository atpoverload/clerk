load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive")

def clerk_deps():
    """Loads dagger to compile the injection framework."""

    if not native.existing_rule("rules_jvm_external"):
      RULES_JVM_EXTERNAL_TAG = "3.3"
      RULES_JVM_EXTERNAL_SHA = "d85951a92c0908c80bd8551002d66cb23c3434409c814179c0ff026b53544dab"

      http_archive(
          name = "rules_jvm_external",
          strip_prefix = "rules_jvm_external-%s" % RULES_JVM_EXTERNAL_TAG,
          sha256 = RULES_JVM_EXTERNAL_SHA,
          url = "https://github.com/bazelbuild/rules_jvm_external/archive/%s.zip" % RULES_JVM_EXTERNAL_TAG,
      )

    if not native.existing_rule("dagger"):
      DAGGER_TAG = "2.28.1"
      DAGGER_SHA = "9e69ab2f9a47e0f74e71fe49098bea908c528aa02fa0c5995334447b310d0cdd"

      http_archive(
          name = "dagger",
          strip_prefix = "dagger-dagger-%s" % DAGGER_TAG,
          sha256 = DAGGER_SHA,
          urls = ["https://github.com/google/dagger/archive/dagger-%s.zip" % DAGGER_TAG],
      )

    if not native.existing_rule("rules_jmh"):
      http_archive(
        name = "rules_jmh",
        strip_prefix = "buchgr-rules_jmh-6ccf8d7",
        url = "https://github.com/buchgr/rules_jmh/zipball/6ccf8d7b270083982e5c143935704b9f3f18b256",
        type = "zip",
        sha256 = "dbb7d7e5ec6e932eddd41b910691231ffd7b428dff1ef9a24e4a9a59c1a1762d",
      )
