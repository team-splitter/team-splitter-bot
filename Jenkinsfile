pipeline {
    agent any

    stages {
        stage ("build") {
            steps {
                echo 'building the application'
                withMaven() {
                    sh 'mnv clean package'
                }
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