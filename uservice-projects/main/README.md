# Dev hints

Sonar:  
- https://docs.sonarcloud.io/advanced-setup/ci-based-analysis/sonarscanner-for-maven/
```
  # push manually sonar result with jacoco coverage:
  mvnd clean package sonar:sonar
```

Docker:
```bash
  # example: to build images to locally deploy to k8s
  # 1) Optionally change context to work on minikube images
  eval $(minikube docker-env)
  # 2) to build image and push it to local k8s instance
  mvnd jib:dockerBuild -pl host clean install -DskipTests
  mvnd jib:dockerBuild -pl host -Dimage=sinnet.azurecr.io/uservice-projects-host:latest
  mvnd jib:dockerBuild -pl initdb-host clean install -DskipTests
  mvnd jib:dockerBuild -pl initdb-host -Dimage=sinnet.azurecr.io/uservice-projects-initdb

  # example: build local tar file with image
  mvn clean compile jib:buildTar -pl host
```

# Interesting articles:
- https://medium.com/swlh/part-ii-spring-boot-app-development-with-skaffold-kustomize-and-jib-aa663e133558
