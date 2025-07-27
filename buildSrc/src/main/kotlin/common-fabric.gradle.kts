/*
 * Copyright 2025 Enaium
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile


plugins {
    java
    kotlin("jvm")
    id("fabric-loom")
    id("mod-publish")
}

val modName: String by rootProject

base {
    archivesName.set(modName)
}

dependencies {
    implementation(project(":core"))
    minecraft("com.mojang:minecraft:${property("minecraft.version")}")
    modImplementation("net.fabricmc:fabric-loader:${property("fabric.loader.version")}")
}
val modVersion: String by rootProject
version = "${property("minecraft.version")}-${modVersion}"

tasks.processResources {
    from(project(":core").sourceSets.main.get().output)
    inputs.property("currentTimeMillis", System.currentTimeMillis())

    filesMatching("fabric.mod.json") {
        expand(
            mapOf(
                "version" to project.version.toString(),
                "minecraft_version" to properties["minecraft.version"].toString()
                    .let { "${it.subSequence(0, it.lastIndexOf("."))}.x" },
                "java_version" to properties["java.version"].toString(),
            )
        )
    }

    filesMatching("authenticate.mixins.json") {
        expand(
            mapOf(
                "java_version" to properties["java.version"],
                "mixin_list" to file("src/main/java/cn/enaium/authenticate/mixin").listFiles()
                    ?.joinToString(", ", "[", "]") { """"${it.name.substringBeforeLast(".")}"""" }
            )
        )
    }
}

tasks.withType<KotlinCompile> {
    compilerOptions {
        jvmTarget.set(
            properties["java.version"].toString()
                .let { if (it == "8") JvmTarget.JVM_1_8 else JvmTarget.fromTarget(it) })
    }
}

property("java.version").toString().toInt().let {
    tasks.withType<JavaCompile> {
        options.release.set(it)
    }

    java.sourceCompatibility = JavaVersion.toVersion(it)
    java.targetCompatibility = JavaVersion.toVersion(it)
}

afterEvaluate {
    configurations.runtimeClasspath.get().forEach {
        if (it.name.startsWith("sponge-mixin")) {
            tasks.named<JavaExec>("runClient") {
                jvmArgs("-javaagent:${it.absolutePath}")
            }
            tasks.named<JavaExec>("runServer") {
                jvmArgs("-javaagent:${it.absolutePath}")
            }
        }
    }
}