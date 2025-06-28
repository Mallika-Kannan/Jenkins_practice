# Jenkins_practice

# Jenkins-Docker-Maven Setup Guide

## Prerequisites
- Docker installed
- Git installed
- Internet access for Maven/DockerHub

## Steps
1. Build the Jenkins image
   ```bash
   docker build -t myjenkins-maven .
````

2. Run the Jenkins container

   ```bash
   docker run -d --name jenkins-docker \
     -p 8080:8080 -p 50000:50000 \
     -v jenkins_home:/var/jenkins_home \
     -v /var/run/docker.sock:/var/run/docker.sock \
     myjenkins-maven
   ```

3. Access Jenkins at `http://localhost:8080`

4. Configure credentials:

   * GitHub PAT (for code checkout)
   * DockerHub credentials (for push stage)

5. Run your pipeline!

