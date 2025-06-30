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
       withCredentials([string(credentialsId: 'sonarqube-token', variable: 'sonarqube-token')]) {
  sh """
    /var/jenkins_home/tools/hudson.plugins.sonar.SonarRunnerInstallation/SonarScanner/bin/sonar-scanner \
    -Dsonar.projectKey=my-java-app \
    -Dsonar.sources=. \
    -Dsonar.host.url=http://sonarqube:9000 \
    -Dsonar.login=$sonarqube-token
  """
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
          sh 'kubectl apply -f deployment.yaml'
        }
      }
    }
  }
}
