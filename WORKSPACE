workspace(name = "clerk")

# load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive")
load("clerk_deps.bzl", "clerk_deps")
clerk_deps()

# RULES_JVM_EXTERNAL_TAG = "3.3"
# RULES_JVM_EXTERNAL_SHA = "d85951a92c0908c80bd8551002d66cb23c3434409c814179c0ff026b53544dab"
#
# http_archive(
#     name = "rules_jvm_external",
#     strip_prefix = "rules_jvm_external-%s" % RULES_JVM_EXTERNAL_TAG,
#     sha256 = RULES_JVM_EXTERNAL_SHA,
#     url = "https://github.com/bazelbuild/rules_jvm_external/archive/%s.zip" % RULES_JVM_EXTERNAL_TAG,
# )

load("@rules_jvm_external//:defs.bzl", "maven_install")
load("@dagger//:workspace_defs.bzl", "DAGGER_ARTIFACTS", "DAGGER_REPOSITORIES")
maven_install(
    artifacts = [
        "javax.inject:javax.inject:1",
    ],
    repositories = [
        "https://repo1.maven.org/maven2",
    ],
)

maven_install(
    artifacts = DAGGER_ARTIFACTS,
    repositories = DAGGER_REPOSITORIES,
)
