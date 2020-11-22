def dummy_benchmark(name, srcs, deps=[], tags=[], plugins=[], **kwargs):
    """Runs JMH benchmarks from ClerkDummyDriver.
    Implementation dervied from https://github.com/buchgr/rules_jmh.
    """
    plugin_name = "_{}_jmh_annotation_processor".format(name)
    native.java_plugin(
        name = plugin_name,
        deps = ["@rules_jmh_maven//:org_openjdk_jmh_jmh_generator_annprocess"],
        processor_class = "org.openjdk.jmh.generators.BenchmarkProcessor",
        visibility = ["//visibility:private"],
        tags = tags,
    )
    native.java_binary(
        name = name,
        srcs = srcs,
        main_class = "clerk.testing.jmh.ClerkDummyDriver",
        deps = deps + ["//java/clerk/testing/jmh", "@rules_jmh_maven//:org_openjdk_jmh_jmh_core"],
        plugins = plugins + [plugin_name],
        tags = tags,
        **kwargs
    )
