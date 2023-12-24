package src.builds

import jetbrains.buildServer.configs.kotlin.BuildType
import jetbrains.buildServer.configs.kotlin.DslContext
import jetbrains.buildServer.configs.kotlin.buildFeatures.perfmon
import jetbrains.buildServer.configs.kotlin.buildSteps.exec
import jetbrains.buildServer.configs.kotlin.buildSteps.maven

object Deploy : BuildType({
    name = "Deploy"
    description = "Build, test and upload to the server"
    id("Deploy")

    vcs {
        root(DslContext.settingsRoot)
    }

    steps {
        maven {
            name = "Build"
            goals = "clean compile"
            runnerArgs = "-DskipTests"
        }

        maven {
            name = "Unit tests"
            goals = "clean test"
            runnerArgs = "-Dmaven.test.failure.ignore=true -Dtest=*.utit.*Test"
        }

        maven {
            name = "Integration tests"
            goals = "clean test"
            runnerArgs = "-Dmaven.test.failure.ignore=true -Dtest=*.integration.*Test"
        }

        maven {
            name = "Build artifacts"
            goals = "clean install"
            runnerArgs = "-DskipTests"
        }

        exec {
            name = "Run deploy script file"
            formatStderrAsError = true
            path = resolvePathToScript("scripts/deploy.sh")
        }
    }

    features {
        perfmon {
        }
    }
})