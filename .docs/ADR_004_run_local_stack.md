Context: Run local stack of µservices and services locally without touching external environments

Decision: Docker compose to run services, minikube to run application

Hints:
- When using Minikube whe have actually two docker image sets: one is local (*docker image list*), and the second is internal to Minikube (*minikube ssh docker image list*, or *eval $(minikube docker-env) && docker image list && ... and other operations on the context of the second docker*)

Reason: It seems to allow the much control over how to run locally, even more making possible  ModelMapper is widely used, it has integration with [Vavr](https://github.com/vavr-io/vavr-jackson) models and [Proto](https://github.com/HubSpot/jackson-datatype-protobuf)

Consequences: Java, as it is not a script lang, needs some tricks to be able to run from sources. Solution is not a universal, see notes

Notes:
- For Spring, to run with *spring-boot:run* main module and dependencies see https://github.com/spring-projects/spring-boot/issues/3436#issuecomment-236213126
