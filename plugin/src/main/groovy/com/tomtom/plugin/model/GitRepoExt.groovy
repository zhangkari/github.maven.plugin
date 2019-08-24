package com.tomtom.plugin.model

import org.gradle.api.Project;

class GitRepoExt {
    static final String NAME = "gavenGitRepo"
    String organization
    String repository
    String branch
    String type

    String path
    Project project

    GitRepoExt(Project project) {
        this.project = project
        this.branch = "master"
        this.type = "release"
        this.path = "${System.env.HOME}/.gradle/caches"
    }

    File repositoryDir() {
        return procject.file("$path/$origanization/$repository")
    }

    String remoteUrl() {
        return "git@github.com:${organization}/${repository}.git"
    }
}
