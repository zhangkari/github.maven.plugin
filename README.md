# Deploy Artifact to github #

## Usage: ##

**Add in project root build.gradle**


```
buildscript {
    repositories {
        maven {
            url "https://raw.githubusercontent.com/zhangkari/github.maven.plugin/master/repo"
        }
    }
    dependencies {
        classpath 'com.tomtom.plugin:github-maven:0.0.1'
    }
}

```
