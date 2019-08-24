package com.tomtom.plugin


import org.apache.tools.ant.Project
import org.gradle.api.Plugin

class GithubMavenPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        print("github maven deploying...")
        DeployTask.create(project)
    }
}