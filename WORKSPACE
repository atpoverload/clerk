workspace(name = "clerk")

load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive")

RULES_JVM_EXTERNAL_TAG = "3.3"
RULES_JVM_EXTERNAL_SHA = "d85951a92c0908c80bd8551002d66cb23c3434409c814179c0ff026b53544dab"

http_archive(
    name = "rules_jvm_external",
    strip_prefix = "rules_jvm_external-%s" % RULES_JVM_EXTERNAL_TAG,
    sha256 = RULES_JVM_EXTERNAL_SHA,
    url = "https://github.com/bazelbuild/rules_jvm_external/archive/%s.zip" % RULES_JVM_EXTERNAL_TAG,
)

load("@rules_jvm_external//:defs.bzl", "maven_install")

maven_install(
    name = "javax_inject_javax_inject",
    artifacts = [
      "javax.inject:javax.inject:1",
    ],
    repositories = ["https://repo1.maven.org/maven2"],
)

DAGGER_TAG = "2.28.1"
DAGGER_SHA = "9e69ab2f9a47e0f74e71fe49098bea908c528aa02fa0c5995334447b310d0cdd"
http_archive(
    name = "dagger",
    strip_prefix = "dagger-dagger-%s" % DAGGER_TAG,
    sha256 = DAGGER_SHA,
    urls = ["https://github.com/google/dagger/archive/dagger-%s.zip" % DAGGER_TAG],
)

load("@dagger//:workspace_defs.bzl", "DAGGER_ARTIFACTS", "DAGGER_REPOSITORIES")

maven_install(
    artifacts = DAGGER_ARTIFACTS,
    repositories = DAGGER_REPOSITORIES,
)

maven_install(
    name = "org_openjdk_jmh_jmh_core",
    artifacts = [
      "org.openjdk.jmh:jmh-core:1.27",
    ],
    repositories = ["https://repo1.maven.org/maven2"],
)
maven_install(
    name = "org_openjdk_jmh_jmh_generator_annprocess",
    artifacts = [
      "org.openjdk.jmh:jmh-generator-annprocess:1.27"
    ],
    repositories = ["https://repo1.maven.org/maven2"],
)
