pipeline {
    agent any

    tools {
        maven 'maven3.8.6'
        jdk 'OpenJDK11'
    }

    environment {
        dockerhub=credentials('docker_hub')
    }

    triggers {
        pollSCM "* * * * *"
    }

    parameters {
        booleanParam(name: "RELEASE",
                description: "Build a release from current commit.",
                defaultValue: false)
    }

    stages {
        stage('Checkout') {
            steps {
                scmSkip(deleteBuild: false, skipPattern:'.*\\[skip ci\\].*')
            }
        }

        stage ('Initialize') {
            steps {
                sh '''
                    echo "PATH = ${PATH}"
                    echo "M2_HOME = ${M2_HOME}"
                    echo "JAVA_HOME = ${JAVA_HOME}"
                '''
            }
        }
        stage ("build") {
            steps {
                echo 'building the application'
                sh 'mvn clean package'
            }
        }

        stage ("Release") {
            when {
                expression { params.RELEASE }
            }
            steps {
                echo "Releasing version"
//                 script {
//                     currentBuild.displayName = "${VERSION}"
//                 }
//                 withMaven(maven: 'maven3.8.6', mavenSettingsConfig: 'team-splitter-settings') {
                    sh '''
                        release_version=$(mvn help:evaluate -Dexpression=project.version -q -DforceStdout | cut -d- -f1)

                        mvn versions:set -DnewVersion=${release_version}

                        mvn -DskipTests -DskipITs \
                        -Djib.to.tags=${release_version} \
                        -Djib.to.auth.username=$dockerhub_USR \
                        -Djib.to.auth.password=$dockerhub_PSW \
                        clean package -Pdocker-deploy

                        mvn versions:revert


                        git config --global user.email "mukhanov.max@gmail.com"
                        git config --global user.name "Platform"

                        mvn -B -DscmCommentPrefix="[platform] " \
                        -DscmDevelopmentCommitComment="@{prefix} prepare next development iteration [skip ci]" \
                        -DscmReleaseCommitComment="@{prefix} prepare release @{releaseLabel} [skip ci]" \
                        release:prepare
                    '''
//                 sh ''
//                 }
            }
        }
    }

}