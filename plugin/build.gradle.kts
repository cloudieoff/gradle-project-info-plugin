plugins {
    `java-gradle-plugin`
    `maven-publish`
    kotlin("jvm") version "1.9.0" // Для Kotlin поддержки
}

// Убедитесь, что group и version указаны ДО блока gradlePlugin
group = "com.yourname"
version = "1.0.0"

gradlePlugin {
    plugins {
        create("projectInfo") {
            id = "com.yourname.projectinfo"
            implementationClass = "com.yourname.projectinfo.ProjectInfoPlugin"
        }
    }
}

// Важно: добавьте блок repositories
repositories {
    mavenCentral()
}

dependencies {
    implementation("com.github.javaparser:javaparser-symbol-solver-core:3.25.1")
    implementation(kotlin("stdlib-jdk8")) // Для Kotlin
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")
}