pipeline {
  agent any
  triggers {
    githubPush()
  }
   environment {
    DOCKER_IMAGE = 'mallikakannan/hello-java:v1'
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
        sh 'docker build -t $DOCKER_IMAGE .'
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
            docker push $DOCKER_IMAGE
          '''
        }
      }
    }
    stage ('Deploy to Kubernetes') {
      steps {
        script {
  // This block securely injects the secret file into the build
          withCredentials([file(credentialsId: 'kubeconfig-local', variable: 'KUBECONFIG')]) {
          sh 'kubectl apply -f deployment.yaml'
        }
      }
    }
  }
}
