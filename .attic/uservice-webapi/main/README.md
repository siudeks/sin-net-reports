# Build and push to minikube local image
```
```

In situations where all that is needed to build a container image and no push to a registry is necessary (essentially by having set quarkus.container-image.build=true and left quarkus.container-image.push unset - it defaults to false), then this extension creates a container image and registers it with the Docker daemon. This means that although Docker isn’t used to build the image, it is nevertheless necessary. Also note that using this mode, the built container image will show up when executing docker images.
