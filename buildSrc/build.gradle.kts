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

plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
    maven {
        name = "Fabric"
        url = uri("https://maven.fabricmc.net/")
    }
    maven {
        name = "legacy-fabric"
        url = uri("https://repo.legacyfabric.net/repository/legacyfabric/")
    }
    mavenCentral()
}

val kotlinVersion: String by project
val loomVersion: String by project
val modPublishVersion: String by project

dependencies {
    api("org.jetbrains.kotlin:kotlin-gradle-plugin:${kotlinVersion}")
    api("net.fabricmc:fabric-loom:${loomVersion}")
    api("net.legacyfabric:legacy-looming:${loomVersion}")
    api("me.modmuss50.mod-publish-plugin:me.modmuss50.mod-publish-plugin.gradle.plugin:${modPublishVersion}")
}