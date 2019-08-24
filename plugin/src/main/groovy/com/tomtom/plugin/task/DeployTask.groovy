package com.tomtom.plugin.task

import com.tomtom.plugin.model.AchieveExt
import com.tomtom.plugin.model.GitRepoExt
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.tasks.TaskAction

class DeployTask extends DefaultTask {
    static final String NAME = "deployGithub"

    GitRepoExt gitRepo
    AchieveExt achieve

    static void create(Project project) {
        project.extensions.create(AchieveExt.NAME, AchieveExt)
        project.extensions.create(GitRepoExt.NAME, GitRepoExt, project)

        def lib = project.extensions.findByName(AchieveExt.NAME)
        def gitExt = project.extensions.findByName(GitRepoExt.NAME)

        project.task(NAME,
                group: 'githubDeploy',
                type: DeployTask,
                dependsOn: ['build'],
                {
                    gitRepo = lib
                    achieve = gitExt
                })
    }

    @TaskAction
    void gavenDeploy() {
        gitRepo = getGitRepo()
        achieve = getAchieve()
        updateLocalRepo(project, gitRepo)
        println("update local repo OK.")
        uploadLocal()
        println("upload local maven OK.")
        uploadGit()
        println("deploy to github OK.")
    }

    private void updateLocalRepo(project, gitRepo) {
        def repoDir = gitRepo.repositoryDir()
        repoDir.mkdirs()

        try {
            project.exec {
                workingDir repoDir
                executable "git"
                args "init"
            }

            project.exec {
                workingDir repoDir
                executable "git"
                args "remote", "add", "origin", gitRepo.remoteUrl()
            }

            project.exec {
                workingDir repoDir
                executable "git"
                args "pull", "--rebase", "origin", gitRepo.branch
            }
        } catch (Exception e) {
            println(e.toString())
        }
    }

    private void uploadLocal() {
        def uploadLocalTask = project.task('uploadLocal', type: Upload) {
            configuration = project.configurations['archives']
            repositories.mavenDeployer {
                repository(url: "file:${gitRepoExt.repositoryDir()}/releases") {
                    pom.groupId = mLibraryExtension.group
                    pom.artifactId = mLibraryExtension.artifactId
                    pom.version = mLibraryExtension.version
                    pom.packaging = mLibraryExtension.packaging
                    pom.description = mLibraryExtension.description
                }
            }
        }
        uploadLocalTask.execute()
    }

    private void uploadGit() {
        def repoDir = mGitRepoExtension.repositoryDir()
        try {
            project.exec {
                workingDir repoDir
                executable "git"
                args "checkout", gitRepo.branch
            }
            project.exec {
                workingDir repoDir
                executable "git"
                args "add", "-A"
            }
            project.exec {
                workingDir repoDir
                executable "git"
                args "commit", "-m", "$achieve.artifactId ${achieve.version}\n$achieve.description"
            }
            project.exec {
                workingDir repoDir
                executable "git"
                args "push", "-f", "origin", gitRepo.branch
            }
        } catch (Exception e) {
            e.printStackTrace()
        }
    }
}
