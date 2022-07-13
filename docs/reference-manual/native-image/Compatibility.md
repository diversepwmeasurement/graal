---
layout: ni-docs
toc_group: metadata
link_title: Compatibility Guide
permalink: /reference-manual/native-image/metadata/Compatibility/
redirect_from: /$version/reference-manual/native-image/Limitations/
---

# Native Image Compatibility Guide

Native Image uses a different way of compiling a Java application than the traditional Java virtual machine (VM).
It distinguishes between **build time** and **run time**.
At the image build time, the `native-image` builder performs static analysis to find all the methods that are reachable from the entry point of an application.
The builder then compiles these (and only these) methods into an executable binary.
Because of this different compilation model, a Java application can behave somewhat differently when compiled into a native image.

Native Image provides an optimization to reduce the memory footprint and startup time of an application.
This approach relies on a ["closed-world assumption"](NativeImageBasics.md#static-analysis-reachability-and-closed-world-assumption) in which all code is known at build time. That is, no new code is loaded at run time.
As with most optimizations, not all applications are amenable to this approach.
If the `native-image` builder is unable to optimize an application at build time, it generates a so-called "fallback file" that requires a Java VM to run.
We recommend to check [Native Image Basics](NativeImageBasics.md) for a detailed description what happens with your Java application at build and run times.

## Features Requiring Metadata

To be suitable for closed-world assumption, the following Java features generally require metadata to pass to `native-image` at build time. 
This metadata ensures that a native image uses the minimum amount of space necessary.

The compatibility of Native Image with the most popular Java libraries was recently enhanced by publishing [shared reachability metadata on GitHub](https://github.com/oracle/graalvm-reachability). The users can share the burden of maintaining metadata for third-party dependencies and reuse it.
See [Reachability Metadata](ReachabilityMetadata.md) to learn more.

## Features Incompatible with Closed-World Assumption

Some Java features are not yet supported within the closed-world assumption, and if used, result in a fallback file.

### `invokedynamic` Bytecode and Method Handles

Under the closed-world assumption, all methods that are called and their call sites must be known.
The `invokedynamic`method and method handles can introduce calls at run time or change the method that is invoked.

Note that `invokedynamic` use cases generated by `javac` for, for example, Java lambda expressions and String concatenation that are supported because they do not change called methods at run time.

### Security Manager

The Java security manager is no longer recommended as a way to isolate less trusted code from more trusted code in the same process.
This is because almost all typical hardware architectures are susceptible to side-channel attacks to access data that is restricted via the security manager.
Using separate processes is now recommended for these cases.

## Features That May Operate Differently in a Native Image

Native Image implements some Java features differently to the Java VM.

### Signal Handlers

Registering a signal handler requires a new thread to start that handles the signal and invokes shutdown hooks.
By default, no signal handlers are registered when building a native image, unless they are registered explicitly by the user.
For example, it is not recommended to register the default signal handlers when building a shared library, but it is desirable to include signal handlers when building a native executable for containerized environments, such as Docker containers.

To register the default signal handlers, pass the `--install-exit-handlers` option to the `native-image` builder.
This option gives you the same signal handlers as a Java VM.

### Class Initializers

By default, classes are initialized at run time.
This ensures compatibility, but limits some optimizations.
For faster startup and better peak performance, it is better to initialize classes at build time.
Class initialization behavior can be specified using the options `--initialize-at-build-time` or `--initialize-at-run-time` for specific classes and packages or for all classes.
Classes that are members of the JDK class libraries are initialized by default.

**Note**: Class initialization at build time may break specific assumptions in existing code.
For example, files loaded in a class initializer may not be in the same place at build time as at run time.
Also, certain objects such as a file descriptors or running threads must not be stored in a native executable.
If such objects are reachable at build time, the `native image` builder fails with an error.

For more information, see [Class Initialization in Native Image](ClassInitialization.md).

### Finalizers

The Java base class `java.lang.Object` defines the method `finalize()`.
It is called by the garbage collector on an object when garbage collection determines that there are no more references to the object.
A subclass can override the `finalize()` method to dispose of system resources or to perform other cleanup operations.

Finalizers have been deprecated since Java SE 9.
They are complicated to implement, and have badly designed semantics.
For example, a finalizer can cause an object to be reachable again by storing a reference to it in a static field.
Therefore, finalizers are not invoked.
We recommend you replace finalizers with weak references and reference queues.

### Threads

Native Image does not implement long-deprecated methods in `java.lang.Thread` such as `Thread.stop()`.

### Unsafe Memory Access

Fields that are accessed using `sun.misc.Unsafe` need to be marked as such for the static analysis if classes are initialized at build time.
In most cases, that happens automatically: field offsets stored in `static final` fields are automatically rewritten from the hosted value (the field offset for the Java VM on which the `native image` builder is running) to the native executable value, and as part of that rewrite the field is marked as `Unsafe`-accessed.
For non-standard patterns, field offsets can be recomputed manually using the annotation `RecomputeFieldValue`.

### Debugging and Monitoring

Java has some optional specifications that a Java implementation can use for debugging and monitoring Java programs, including JVMTI.
They help you monitor the Java VM at runtime for events such as compilation, for example, which do not occur in most native images.
These interfaces are built on the assumption that Java bytecodes are available at run time, which is not the case for native images built with the closed-world optimization.
Because the `native-image` builder generates a native executable, users must use native debuggers and monitoring tools (such as GDB or VTune) rather than tools targeted for Java.
JVMTI and other bytecode-based tools are not supported with Native Image.

# Limitations on Linux ARM64 Architecture

Mostly all Native Image features are supported on Linux ARM 64-bit architecture, except for the limitations described below.

* `-R:[+|-]WriteableCodeCache`: must be disabled.
* `--libc=<value>`: `musl` is not supported.
* `--gc=<value>`: The G1 garbage collector (`G1`) is not supported.

Find a complete list of options to the `native-image` builder [here](BuildOptions.md).

### Further Reading

* [Class Initialization in Native Image](ClassInitialization.md)
* [Reachability Metadata](ReachabilityMetadata.md)
* [GraalVM Reachability Metadata Repository](https://github.com/oracle/graalvm-reachability)