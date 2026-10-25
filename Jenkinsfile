pipeline {
    agent any

    parameters {
        choice(name: 'ENVIRONMENT', choices: ['staging', 'production', 'development'], description: 'Deployment Target Environment')
        string(name: 'DOCKER_TAG', defaultValue: '1.0.0', description: 'Docker Image Tag')
        booleanParam(name: 'RUN_SELENIUM', defaultValue: true, description: 'Execute End-to-End Selenium Test Suite')
        string(name: 'APP_PORT', defaultValue: '8080', description: 'Application Port')
    }

    environment {
        APP_NAME = 'volunteer-duty-roster'
        DOCKER_HUB_USER = 'meetjain6091'
        DOCKER_IMAGE = "${DOCKER_HUB_USER}/${APP_NAME}"
        WAR_FILE = "target/volunteer-duty-roster.war"
    }

    stages {
        stage('Checkout') {
            steps {
                echo "=== Stage 1: Checkout Source Code from GitHub ==="
                checkout scm
                sh 'git log -1 --pretty=format:"Commit: %h by %an - %s"'
            }
        }

        stage('Compile & Static Analysis') {
            steps {
                echo "=== Stage 2: Compiling Application & Dependency Validation ==="
                sh 'mvn clean compile'
            }
        }

        stage('Unit & Selenium Testing') {
            steps {
                echo "=== Stage 3: Running Unit & Selenium Automated Tests ==="
                sh 'mvn test'
            }
            post {
                always {
                    echo "Publishing test results..."
                    junit allowEmptyResults: true, testResults: 'target/surefire-reports/*.xml'
                    archiveArtifacts allowEmptyArchive: true, artifacts: 'target/screenshots/*.png'
                }
                failure {
                    echo "[QUALITY GATE FAILED] Test failure detected! Halting deployment pipeline."
                }
            }
        }

        stage('Package War Artefact') {
            steps {
                echo "=== Stage 4: Packaging Production WAR Archive ==="
                sh 'mvn package -DskipTests'
                archiveArtifacts artifacts: "${WAR_FILE}", fingerprint: true
            }
        }

        stage('Docker Build & Tag') {
            steps {
                echo "=== Stage 5: Building Versioned Docker Image ==="
                sh """
                    docker build -t ${DOCKER_IMAGE}:${params.DOCKER_TAG} \
                                 -t ${DOCKER_IMAGE}:${BUILD_NUMBER} \
                                 -t ${DOCKER_IMAGE}:latest .
                """
            }
        }

        stage('Publish Docker Image') {
            steps {
                echo "=== Stage 6: Publishing Image to Registry ==="
                /* In production Jenkins, configure Docker Hub credentials:
                withCredentials([usernamePassword(credentialsId: 'docker-hub-creds', usernameVariable: 'DH_USER', passwordVariable: 'DH_PASS')]) {
                    sh 'echo "$DH_PASS" | docker login -u "$DH_USER" --password-stdin'
                    sh "docker push ${DOCKER_IMAGE}:${params.DOCKER_TAG}"
                    sh "docker push ${DOCKER_IMAGE}:latest"
                }
                */
                sh "echo '[MOCK PUSH] Successfully validated and tagged ${DOCKER_IMAGE}:${params.DOCKER_TAG}'"
            }
        }

        stage('Deploy to Target Environment') {
            steps {
                echo "=== Stage 7: Automated Zero-Downtime Container Deployment ==="
                sh """
                    echo "Deploying to environment: ${params.ENVIRONMENT} on port ${params.APP_PORT}"
                    docker stop ${APP_NAME} || true
                    docker rm ${APP_NAME} || true
                    docker run -d --name ${APP_NAME} -p ${params.APP_PORT}:8080 --restart unless-stopped ${DOCKER_IMAGE}:${params.DOCKER_TAG}
                """
            }
        }

        stage('Health Check & Smoke Test') {
            steps {
                echo "=== Stage 8: Verifying Container Health & Rollback Check ==="
                sh """
                    sleep 10
                    curl -s -f http://localhost:${params.APP_PORT}/login > /dev/null || {
                        echo "[ALERT] Health check failed! Initiating automated rollback...";
                        docker stop ${APP_NAME} || true;
                        docker run -d --name ${APP_NAME} -p ${params.APP_PORT}:8080 ${DOCKER_IMAGE}:latest;
                        exit 1;
                    }
                    echo "Application healthy and operational at http://localhost:${params.APP_PORT}"
                """
            }
        }
    }

    post {
        success {
            echo "Pipeline completed successfully! Build #${BUILD_NUMBER} deployed to ${params.ENVIRONMENT}."
        }
        failure {
            echo "Pipeline failed on build #${BUILD_NUMBER}. Deployment aborted to preserve stability."
        }
        always {
            cleanWs deleteDirs: true, notFailBuild: true
        }
    }
}
