workspace(name = "clerk")

load("clerk_deps.bzl", "clerk_deps")
clerk_deps()

load("@rules_jvm_external//:defs.bzl", "maven_install")
load("@dagger//:workspace_defs.bzl", "DAGGER_ARTIFACTS", "DAGGER_REPOSITORIES")
maven_install(
    artifacts = DAGGER_ARTIFACTS,
    repositories = DAGGER_REPOSITORIES,
)

load("@rules_jmh//:deps.bzl", "rules_jmh_deps")
rules_jmh_deps()

load("@rules_jmh//:defs.bzl", "rules_jmh_maven_deps")
rules_jmh_maven_deps()
