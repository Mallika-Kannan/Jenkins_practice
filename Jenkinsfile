pipeline {
  agent any
  triggers {
    githubPush()
  }
  environment {
    // Define variables to use throughout the pipeline
    DOCKER_IMAGE_NAME = "mallikakannan/hello-java"
    DOCKER_IMAGE_TAG  = "v${env.BUILD_NUMBER}"
  }
  stages {
    stage ('Checkout') {
      steps {
        git(
          url: 'https://github.com/Mallika-Kannan/Jenkins_practice.git',
          branch: 'feature-branch-jenkine-v1',
          credentialsId: 'githubPAT'
        )
      }
    }

    stage ('Build') {
      steps {
        sh 'mvn clean package'
      }
    }
    stage ('Sonarqube Analysis') {
      steps {
        withSonarQubeEnv('sonarqube-local') {
          sh '''
            sonar-scanner -X \
            -Dsonar.projectKey=my-java-app \
            -Dsonar.sources=. \
            -Dsonar.host.url=http://host.docker.internal:9000
          '''
        }
      }
    }
    stage ('Build Docker image') {
      steps {
        // Use double quotes to allow Groovy variables like ${...} to be replaced
        sh "docker build -t ${DOCKER_IMAGE_NAME}:${DOCKER_IMAGE_TAG} ."
      }
    }

    stage ('Push docker image') {
      steps {
        withCredentials([
          usernamePassword(
            credentialsId: 'dockerhub-credentials',
            usernameVariable: 'DOCKER_USER',
            passwordVariable: 'DOCKER_PASS'
          )
        ]) {
          // Use triple-double quotes so variables like ${DOCKER_IMAGE_NAME} work
          sh """
            echo "$DOCKER_PASS" | docker login -u "$DOCKER_USER" --password-stdin
            docker push "${DOCKER_IMAGE_NAME}:${DOCKER_IMAGE_TAG}"
          """
        }
      }
    }
    stage ('Deploy to Kubernetes') {
      steps {
        // This block securely injects the secret file into the build
        withCredentials([file(credentialsId: 'kubeconfig-local', variable: 'KUBECONFIG')]) {
          // The entire 'helm' command must be inside a single sh step
          sh """
            helm upgrade --install my-java-app-release ./my-java-app \\
              --set image.tag=${DOCKER_IMAGE_TAG}
          """
        }
      }
    }
  }
}
