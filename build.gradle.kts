plugins {
    val kotlinVersion = "1.6.0"
    java
    kotlin("jvm") version kotlinVersion
    kotlin("kapt") version kotlinVersion
    `maven-publish`
}

group = "uos.dev"
version = "1.7.4"

repositories {
    mavenCentral()
}
java {
    withSourcesJar()
    sourceCompatibility = JavaVersion.VERSION_1_8
}
kapt {
    arguments {
        arg("project", "${project.group}/${project.name}")
    }
}
publishing {
    publications {
        create<MavenPublication>("restcli") {
            from(components["java"])
        }
    }
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    val junitVersion = "5.6.0"
    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-params:$junitVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    implementation("info.picocli:picocli:4.3.2")
    kapt("info.picocli:picocli-codegen:4.3.2")
    testImplementation("com.google.truth:truth:1.0.1")
    implementation("com.squareup.okhttp3:okhttp:4.7.2")
    implementation("com.google.code.gson:gson:2.8.6")
    implementation("org.apache.commons:commons-text:1.9")
    implementation("com.squareup.okhttp3:logging-interceptor:4.8.0")
    implementation("commons-validator:commons-validator:1.6")
    implementation("com.jakewharton.picnic:picnic:0.5.0")
    implementation("com.github.ajalt:mordant:1.2.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.8")
    implementation("io.github.microutils:kotlin-logging:1.8.3")
    implementation("org.apache.logging.log4j:log4j-slf4j-impl:2.13.3")
    val graalvmVersion = "21.1.0"
    implementation("org.graalvm.js:js:$graalvmVersion") { exclude("org.graalvm.regex") }
    implementation("org.graalvm.js:js-scriptengine:$graalvmVersion")
}

tasks {
    test {
        useJUnitPlatform()
    }
    jar {
        manifest {
            attributes["Main-Class"] = "uos.dev.restcli.AppKt"
            attributes["Multi-Release"] = true
        }
        archiveBaseName.set("restcli")
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        from(configurations.runtimeClasspath.get().map {
            if (it.isDirectory) it else zipTree(it)
        })
    }
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}
