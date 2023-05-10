# Including extra files

There are cases when additional files (other than ones produced by the Quarkus build) need to be added to a container image. To support these cases, Quarkus copies any file under src/main/jib into the built container image (which is essentially the same idea that the Jib Maven and Gradle plugins support). For example, the presence of src/main/jib/foo/bar would result in /foo/bar being added into the container filesystem.

[source](https://quarkus.io/guides/container-image#including-extra-files)
