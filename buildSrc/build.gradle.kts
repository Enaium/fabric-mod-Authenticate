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
    api(libs.kotlin)
    api(libs.loom)
    api(libs.legacy.looming)
    api(libs.publish)
}