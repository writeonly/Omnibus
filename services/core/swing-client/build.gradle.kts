plugins {
    kotlin("jvm") version "2.1.21"
    application

    id("com.google.protobuf") version "0.9.4"
}

group = "pl.writeonly.omnibus"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {

    // Swing UI (JDK built-in) → brak dependency

    // gRPC
    implementation("io.grpc:grpc-netty-shaded:1.63.0")
    implementation("io.grpc:grpc-protobuf:1.63.0")
    implementation("io.grpc:grpc-stub:1.63.0")

    // protobuf runtime
    implementation("com.google.protobuf:protobuf-java:3.25.5")

    // annotation (required by grpc)
    compileOnly("javax.annotation:javax.annotation-api:1.3.2")

    // logging
    implementation("io.github.microutils:kotlin-logging:3.0.5")

    // optional JSON (if mixed APIs)
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.17.0")

    testImplementation(kotlin("test"))
}

application {
    mainClass.set("pl.writeonly.omnibus.client.MainApp")
}
