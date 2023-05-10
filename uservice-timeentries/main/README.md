# Dev notes

## Profiles
We support some profiles in backend code to allow run them with different configurations:
- dev - no security
- prod - ready for deployment, postgresql database

## Good development practices

- **[mvn versions:display-dependency-updates](https://www.mojohaus.org/versions-maven-plugin/display-dependency-updates-mojo.html)** to display all dependencies that have newer versions available
- **[mvn versions:display-plugin-updates](https://www.mojohaus.org/versions-maven-plugin/display-plugin-updates-mojo.html)** to display all plugins that have newer versions available, taking care of Maven version prerequisites
- **[mvn project-info-reports:licenses]

<https://www.freecodecamp.org/news/cjn-junit-test-with-maven-in-vscode/>
https://github.com/SonarSource/sonar-scanning-examples/blob/master/doc/jacoco.md


## Used articles
- https://www.vinsguru.com/grpc-client-streaming-api-in-java/
- https://github.com/grpc/grpc-java/blob/master/README.md
- Integrate [Micrometer](https://developer.ibm.com/technologies/java/tutorials/monitor-spring-boot-microservices/)
- [How to design a good JWT authentication filter](https://stackoverflow.com/questions/41975045/how-to-design-a-good-jwt-authentication-filter)
- https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods.query-creation

## Coding principles

Docker:
```bash
  # example: to build images to locally deploy to k8s
  # 1) Optionally change context to work on minikube images
  eval $(minikube docker-env)
  # 2) to build image and push it to local k8s instance
  mvnd -pl host -am clean install -DskipTests
  mvnd jib:dockerBuild -pl host -Dimage=sinnet.azurecr.io/uservice-timeentries-host:latest
  mvnd -pl initdb-host -am clean install -DskipTests
  mvnd jib:dockerBuild -pl initdb-host -Dimage=sinnet.azurecr.io/uservice-timeentries-initdb:latest

  # example: build local tar file with image
  mvn clean compile jib:buildTar -pl host
```

