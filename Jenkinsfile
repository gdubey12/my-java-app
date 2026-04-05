pipeline {
    // Run inside a Maven Docker container — no Maven needed on host
    agent {
        docker {
            image 'maven:3.9-eclipse-temurin-17'
            args  '-v $HOME/.m2:/root/.m2'  // cache deps between builds
        }
    }

    options {
        timestamps()                              // timestamp every log line
        timeout(time: 15, unit: 'MINUTES')        // fail if build hangs
        buildDiscarder(logRotator(numToKeepStr: '10')) // keep last 10 builds
    }

    // Trigger: poll Git every 2 minutes (use webhook in production)
    triggers {
        pollSCM('H/2 * * * *')
    }

    stages {
        stage('Checkout info') {
            steps {
                // Show what commit triggered this build
                sh 'git log -1 --format="%h | %s | by %an on %ad"'
                sh 'git branch --show-current || echo "detached HEAD"'
            }
        }

        stage('Compile') {
            steps {
                sh 'mvn -B clean compile'
                echo "Compiled successfully with Maven"
            }
        }

        stage('Test') {
            steps {
                sh 'mvn -B test'
            }
            post {
                always {
                    // Publish test results as a clickable report in the UI
                    junit '**/target/surefire-reports/*.xml'
                }
            }
        }

        stage('Package') {
            steps {
                sh 'mvn -B package -DskipTests'
                // Archive artifact so it can be downloaded from Jenkins UI
                archiveArtifacts artifacts: 'target/*.jar',
                                  fingerprint: true
                echo "JAR archived — find it under Build Artifacts in the UI"
            }
        }
    }

    post {
        always   { cleanWs() }
        success  { echo "Pipeline SUCCESS — commit: ${GIT_COMMIT?.take(7)}" }
        failure  { echo "Pipeline FAILED — check the stage above" }
    }
}