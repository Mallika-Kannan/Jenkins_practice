FROM jenkins/jenkins:lts

USER root

# Install Maven, curl, gnupg, lsb-release (for Docker repo setup)
RUN apt-get update && \
    apt-get install -y \
        maven \
        curl \
        gnupg2 \
        lsb-release \
        software-properties-common

# Add Docker's official GPG key and set up stable repo
RUN curl -fsSL https://download.docker.com/linux/debian/gpg | gpg --dearmor -o /usr/share/keyrings/docker-archive-keyring.gpg && \
    echo "deb [arch=amd64 signed-by=/usr/share/keyrings/docker-archive-keyring.gpg] https://download.docker.com/linux/debian $(lsb_release -cs) stable" > /etc/apt/sources.list.d/docker.list && \
    apt-get update && \
    apt-get install -y docker-ce-cli

# Clean up apt cache to reduce image size
RUN apt-get clean && rm -rf /var/lib/apt/lists/*

USER jenkins
