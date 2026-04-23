plugins {
    // keep root as an aggregator; per-module applies what it needs
}

allprojects {
    group = "com.omnibus"
    version = "0.0.1-SNAPSHOT"

    repositories {
        mavenCentral()
    }
}

