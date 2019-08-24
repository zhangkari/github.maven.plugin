package com.tomtom.plugin


import org.apache.tools.ant.Project
import org.gradle.api.Plugin

class GithubDeploy implements Plugin<Project> {

    @Override
    void apply(Project project) {
        print("github deploying...")
//        DeployTask.create(project)

        project.task('github-maven-task') << {
            println 'everyone is ready !'
        }
    }
}