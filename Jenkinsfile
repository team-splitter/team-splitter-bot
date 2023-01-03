pipeline {
    agent any

    tools {
        maven 'maven3.8.6'
        jdk 'OpenJDK11'
    }

    environment {
        VERSION = "${env.BUILD_ID}"
        dockerhub=credentials('docker_hub')
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
//                 script {
//                     currentBuild.displayName = "${VERSION}"
//                 }
//                 sh 'mvn deploy scm:tag -Drevision=${VERSION}'
                sh 'mvn -DskipTests -DskipITs -Djib.to.tags=${VERSION} -Djib.to.auth.username=$dockerhub_USR -Djib.to.auth.password=$dockerhub_PSW clean package -Pdocker-deploy'
            }
        }
    }
}