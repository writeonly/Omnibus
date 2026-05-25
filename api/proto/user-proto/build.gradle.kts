import com.google.protobuf.gradle.*

plugins {
    kotlin("jvm")
    kotlin("plugin.spring")

    alias(libs.plugins.protobuf)
}

repositories {
    mavenCentral()
}

dependencies {
    api(libs.protobuf.java)
    api(libs.protobuf.kotlin)

    api(libs.grpc.stub)
    api(libs.grpc.protobuf)
    api(libs.grpc.netty.shaded)

    api(libs.grpc.kotlin.stub)

    compileOnly(libs.javax.annotation.api)
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:${libs.versions.protobuf.get()}"
    }

    plugins {
        id("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:${libs.versions.grpc.get()}"
        }
    }

    generateProtoTasks {
        all().forEach { task ->
            task.plugins {
                id("grpc")
            }
        }
    }
}