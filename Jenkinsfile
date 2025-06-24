pipeline {
  agent {
    docker {
      image'maven:3.9.6-eclipse-temurin-17'
    }
  }
 stages {
  stage ('Checkout'){
   steps {
    git url: 'https://github.com/Mallika-Kannan/Jenkins_practice', branch: 'feature-branch-jenkine-v1'
    }
    }

  stage ('Build'){
      steps {
        sh 'mvn clean package'
      }
    }
  }
}
