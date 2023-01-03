pipeline {
    agent any

    tools {
        maven 'maven3.8.6'
        jdk 'OpenJDK11'
    }

    environment {
        dockerhub=credentials('docker_hub')
    }

    parameters {
        booleanParam(name: "RELEASE",
                description: "Build a release from current commit.",
                defaultValue: false)
    }

    stages {
        stage('Checkout') {
            steps {
                scmSkip(deleteBuild: true, skipPattern:'.*\\[skip ci\\].*')
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
//                 sh 'mvn -B -DscmCommentPrefix="[platform] " -DscmDevelopmentCommitComment="@{prefix} prepare next development iteration [skip ci]" -DscmReleaseCommitComment="@{prefix} prepare release @{releaseLabel} [skip ci]" release:prepare'
//                 sh 'mvn -DskipTests -DskipITs -Djib.to.tags=${VERSION} -Djib.to.auth.username=$dockerhub_USR -Djib.to.auth.password=$dockerhub_PSW clean package -Pdocker-deploy'
            }
        }
    }

}