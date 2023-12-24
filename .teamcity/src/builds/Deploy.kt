package src.builds

import jetbrains.buildServer.configs.kotlin.BuildType
import jetbrains.buildServer.configs.kotlin.DslContext
import jetbrains.buildServer.configs.kotlin.buildFeatures.perfmon
import jetbrains.buildServer.configs.kotlin.buildSteps.SSHUpload
import jetbrains.buildServer.configs.kotlin.buildSteps.exec
import jetbrains.buildServer.configs.kotlin.buildSteps.maven
import jetbrains.buildServer.configs.kotlin.buildSteps.sshExec
import jetbrains.buildServer.configs.kotlin.buildSteps.sshUpload

object Deploy : BuildType({
    name = "Deploy"
    description = "Build, test and upload to the server"
    id("Deploy")

    vcs {
        root(DslContext.settingsRoot)
    }

    steps {
        maven {
            name = "Compile source code"
            goals = "clean compile"
            runnerArgs = "-DskipTests"
        }

        maven {
            name = "Unit tests"
            goals = "clean test"
            runnerArgs = "-Dmaven.test.failure.ignore=true -Dtest=*.unit.*Test"
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
            name = "Prepare source files for uploading"
            path = resolvePathToScript("scripts/prepare.sh")
        }

        sshUpload {
            name = "Upload app source to the server"
            id = "UploadAppSources"
            transportProtocol = SSHUpload.TransportProtocol.SCP
            sourcePath = """
                app_sources/*
            """.trimIndent()
            targetUrl = "192.168.0.99:demo-app/target"
            authMethod = uploadedKey {
                username = "ytty"
                key = "Keys for app server connection"
            }
        }

        sshExec {
            name = "Run deploy script on the server"
            id = "RunDeployScript"
            commands = """
                echo "Running launch.sh ..."
                cd ./demo-app/target
                chmod 777 ./launch.sh
                ./launch.sh
            """.trimIndent()
            targetUrl = "192.168.0.99"
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