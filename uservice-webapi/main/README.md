# Dev hints

Sonar:  
- https://docs.sonarcloud.io/advanced-setup/ci-based-analysis/sonarscanner-for-maven/
```
  # push manually sonar result with jacoco coverage:
  mvnd clean package sonar:sonar
```

Docker:  
```bash
  # example
  # 1) Optionally change context to work on minikube images
  eval $(minikube docker-env)
  # 2) to build image and push it to local k8s instance
  mvnd clean install jib:dockerBuild -Dimage=sinnet.azurecr.io/uservice-webapi-host:latest -DskipTests

```


# Interesting articles:
- https://medium.com/swlh/part-ii-spring-boot-app-development-with-skaffold-kustomize-and-jib-aa663e133558
