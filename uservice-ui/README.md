Build and push local version of the app

```bash
eval $(minikube docker-env)
docker build . -t sinnet.azurecr.io/webapp:latest
```
