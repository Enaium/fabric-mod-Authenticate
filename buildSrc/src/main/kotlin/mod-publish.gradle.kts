import me.modmuss50.mpp.PublishModTask
import org.gradle.util.internal.VersionNumber

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
    id("me.modmuss50.mod-publish-plugin")
}

afterEvaluate {
    publishMods {
        val disableObfuscation = properties.getOrDefault("fabric.loom.disableObfuscation", false).toString().toBoolean()
        val minecraftVersion = properties["minecraft.version"].toString()
        val modern = VersionNumber.parse(minecraftVersion) >= VersionNumber.parse("1.14")
        file = tasks.named<AbstractArchiveTask>(if (disableObfuscation) "jar" else "remapJar").get().archiveFile.get()
        type = STABLE
        displayName = "Authenticate ${project.version}"
        changelog = rootProject.file("changelog.md").readText(Charsets.UTF_8)
        modLoaders.add("fabric")

        curseforge {
            projectId = "1315021"
            accessToken = providers.gradleProperty("curseforge.token")
            minecraftVersions.add(property("minecraft.version").toString())
            requires(
                "fabric-language-kotlin",
                "fabric-database-h2",
                "fabric-orm-jimmer",
                if (modern) "fabric-api" else "legacy-fabric-api"
            )
        }

        modrinth {
            projectId = "bsrcaiiY"
            accessToken = providers.gradleProperty("modrinth.token")
            minecraftVersions.add(property("minecraft.version").toString())
            requires(
                "fabric-language-kotlin",
                "fabric-database-h2",
                "fabric-orm-jimmer",
                if (modern) "fabric-api" else "legacy-fabric-api"
            )
        }

        github {
            repository = "Enaium/fabric-mod-Authenticate"
            accessToken = providers.gradleProperty("github.token")
            commitish = "master"
        }

        tasks.withType<PublishModTask>().configureEach {
            dependsOn(tasks.named(if (disableObfuscation) "jar" else "remapJar"))
        }
    }
}