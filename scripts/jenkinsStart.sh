docker run -p 8090:8080 -p 50000:50000 -d \
  --name jenkins
  -v jenkins_home:/var/jenkins_home
  -v /var/run/docker.sock:/var/run/docker.sock \
  --restart unless-stopped  jenkins/jenkins:lts