pipeline {
    agent any
    tools {
        maven 'maven3.8.6'
        jdk 'OpenJDK11.0.1'
    }

    stages {
        stage ('Initialize') {
            steps {
                sh '''
                    echo "PATH = ${PATH}"
                    echo "M2_HOME = ${M2_HOME}"
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
            steps {
                echo 'deploying the application'
            }
        }
    }
}