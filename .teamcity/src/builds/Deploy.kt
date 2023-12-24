package src.builds

import jetbrains.buildServer.configs.kotlin.BuildType
import jetbrains.buildServer.configs.kotlin.DslContext
import jetbrains.buildServer.configs.kotlin.buildFeatures.perfmon
import jetbrains.buildServer.configs.kotlin.buildSteps.SSHUpload
import jetbrains.buildServer.configs.kotlin.buildSteps.maven
import jetbrains.buildServer.configs.kotlin.buildSteps.sshUpload

object Deploy : BuildType({
    name = "Deploy"
    description = "Build, test and upload to the server"
    id("Deploy")

    vcs {
        root(DslContext.settingsRoot)
    }

    steps {
//        maven {
//            name = "Build"
//            goals = "clean compile"
//            runnerArgs = "-DskipTests"
//        }
//
//        maven {
//            name = "Unit tests"
//            goals = "clean test"
//            runnerArgs = "-Dmaven.test.failure.ignore=true -Dtest=*.unit.*Test"
//        }
//
//        maven {
//            name = "Integration tests"
//            goals = "clean test"
//            runnerArgs = "-Dmaven.test.failure.ignore=true -Dtest=*.integration.*Test"
//        }

        maven {
            name = "Build artifacts"
            goals = "clean install"
            runnerArgs = "-DskipTests"
        }

        sshUpload {
            name = "UploadJavaPackages"
            id = "__NEW_RUNNER__"
            transportProtocol = SSHUpload.TransportProtocol.SCP
            sourcePath = """
                target/demo-*.jar
                scripts/launch.sh
            """.trimIndent()
            targetUrl = "192.168.0.99:demo-app/target"
            authMethod = uploadedKey {
                username = "ytty"
                key = "Keys for app server connection"
            }
        }
    }

    features {
        perfmon {
        }
    }
})