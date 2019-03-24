import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.3.21"
    id("com.commercehub.gradle.plugin.avro") version "0.16.0"
}

group = "com.riywo.ninja"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    compile("org.apache.avro:avro:1.8.2")
    compile("org.slf4j:slf4j-api:1.7.26")
    compile("ch.qos.logback:logback-core:1.2.3")
    compile("ch.qos.logback:logback-classic:1.2.3")
    compile("io.github.microutils:kotlin-logging:1.6.24")

    testCompile("org.junit.jupiter:junit-jupiter-api:5.4.0")
    testCompile("org.junit.jupiter:junit-jupiter-params:5.4.0")
    testCompile("org.assertj:assertj-core:3.11.1")
    testRuntime("org.junit.jupiter:junit-jupiter-engine:5.4.0")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.withType<Test> {
    useJUnitPlatform()
    systemProperties = mapOf(
        "junit.jupiter.testinstance.lifecycle.default" to "per_class"
    )
}
