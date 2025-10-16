plugins {
    java
    application
    id("com.gradleup.shadow") version "9.2.2"
}

group = "klaxon.klaxon.gluebox"
version = "0.0.0"

repositories {
    mavenCentral()
}

val lwjglVersion = "3.3.6"
val lwjglNatives = "natives-linux"

repositories {
    mavenCentral()
}

dependencies {
    implementation(platform("org.lwjgl:lwjgl-bom:$lwjglVersion"))

    implementation("org.lwjgl", "lwjgl")
    implementation("org.lwjgl", "lwjgl-glfw")
    implementation("org.lwjgl", "lwjgl-opengl")
    implementation("org.lwjgl", "lwjgl", classifier = lwjglNatives)
    implementation("org.lwjgl", "lwjgl-glfw", classifier = lwjglNatives)
    implementation("org.lwjgl", "lwjgl-opengl", classifier = lwjglNatives)

    implementation("org.apache.logging.log4j:log4j-api:2.12.4")
    implementation("org.apache.logging.log4j:log4j-core:2.12.4")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

application {
    applicationDefaultJvmArgs = listOf("-ea", "--enable-native-access=ALL-UNNAMED")
    mainClass = "klaxon.klaxon.gluebox.Main"
}

tasks.test {
    jvmArgs = listOf("--enable-native-access=ALL-UNNAMED")
    useJUnitPlatform()
}