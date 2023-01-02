pipeline {
    agent any
    tools {
        maven 'maven3.8.6'
        jdk 'OpenJDK11'
    }

    environment {
        VERSION = "1.0.${env.BUILD_ID}"
    }

    stages {
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

        stage ("test") {
            steps {
                echo 'testing the application'
            }
        }

        stage ("deploy") {
            when {
                branch 'main'
            }
            steps {
                echo "deploying the application version ${VERSION}"
                script {
                    currentBuild.displayName = "${VERSION}"
                }
//                 sh 'mvn deploy scm:tag -Drevision=${VERSION}'
            }
        }
    }
}