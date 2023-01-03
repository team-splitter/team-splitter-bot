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
        stage('Run CI?') {
//           agent any
          steps {
            script {
              if (sh(script: "git log -1 --pretty=%B | fgrep -ie '[skip ci]' -e '[ci skip]'", returnStatus: true) == 0) {
                currentBuild.result = 'NOT_BUILT'
                error 'Aborting because commit message contains [skip ci]'
              }
            }
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
                sh 'echo $dockerhub_PSW | docker login -u $dockerhub_USR --password-stdin'
                sh 'mvn -DskipTests -DskipITs -Djib.to.tags=${VERSION} clean package -Pdocker-deploy'
            }
        }
    }
}