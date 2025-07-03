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
        def dockerImage = "mallikakannan/hello-java:${params.IMAGE_VERSION}"
        sh 'docker build -t ${DOCKER_IMAGE_NAME}:${DOCKER_IMAGE_TAG} .'
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
          sh '''
            echo "$DOCKER_PASS" | docker login -u "$DOCKER_USER" --password-stdin
            docker push ${DOCKER_IMAGE_NAME}:${DOCKER_IMAGE_TAG}
          '''
        }
      }
    }
    stage ('Deploy to Kubernetes') {
      steps {
        script {
          sh "sed -i 's|image: .*|image: ${DOCKER_IMAGE_NAME}:${DOCKER_IMAGE_TAG}|g' deployment.yaml"
  // This block securely injects the secret file into the build
          withCredentials([file(credentialsId: 'kubeconfig-local', variable: 'KUBECONFIG')]) {
          sh 'kubectl apply -f deployment.yaml'
        }
      }
    }
  }
}
}
