import org.gradle.api.publish.maven.MavenPublication

plugins {
    `maven-publish`
}

val aarFiles = listOf(
    "${project.rootDir}/libs/dtefmobile-2.6.6-2308141153-linx-debug.aar" to "dtefmobile-debug",
    "${project.rootDir}/libs/dtefmobile-2.6.6-2308141153-linx-release.aar" to "dtefmobile-release",
    "${project.rootDir}/libs/log-manager-1.0.0-210220.aar" to "log-manager",
    "${project.rootDir}/libs/linxpay-sdk-1.1.0-2203201805.aar" to "linxpay-sdk",
)

publishing {
    publications {
        aarFiles.forEach { (filePath, artifactId) ->
            create<MavenPublication>(artifactId) {
                groupId = "linx"
                this.artifactId = artifactId
                version = "1.0.0"

                artifact(filePath)
            }
        }
    }

    repositories {
        maven {
            url = uri("${project.rootDir}/libs/localMaven")
        }
    }
}

// RUN ./gradlew -b publish.gradle.kts publish to update the localMaven repository