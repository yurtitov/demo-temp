import jetbrains.buildServer.configs.kotlin.*
import jetbrains.buildServer.configs.kotlin.buildFeatures.perfmon
import jetbrains.buildServer.configs.kotlin.buildSteps.maven
import jetbrains.buildServer.configs.kotlin.triggers.vcs

version = "2023.11"

project {


    val buildTypes = sequential {
        buildType(Maven("Build", "clean compile"))
        parallel {
            buildType(
                Maven(
                    "Unit Tests",
                    "clean test",
                    "-Dmaven.test.failure.ignore=true -Dtest=*.unit.*Test"
                )
            )
            buildType(
                Maven(
                    "Integration Tests",
                    "clean test",
                    "-Dmaven.test.failure.ignore=true -Dtest=*.integration.*Test"
                )
            )
        }
        buildType(Maven("Package", "clean package"))
    }.buildTypes()

    buildTypes.forEach { buildType(it) }
    buildTypes.last().triggers {
        vcs {

        }
    }
}

class Maven(name: String, goals: String, runnerArgs: String? = null) : BuildType({
    id(name.toId())
    this.name = name

    vcs {
        root(DslContext.settingsRoot)
    }

    steps {
        maven {
            this.goals = goals
            this.runnerArgs = runnerArgs
        }
    }

    features {
        perfmon {
        }
    }
})

fun String.toId(): String {
    return this.replace(' ', '_')
}
