package com.tomtom.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project

class GithubMavenPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        print("github maven deploying...")
        DeployTask.create(project)
    }
}