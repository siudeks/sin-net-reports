#TBD


# One time operations 1:
install shared helm part located in ../infra


```bash
. initshell.sh local
```

# One time operation:
```
# create namespace for local development

```

# One time operation 2:
# docker credentials for local kubernetes to allow pull images
# https://kubernetes.io/docs/tasks/configure-pod-container/pull-image-private-registry/#log-in-to-docker-hub
k create secret docker-registry regcred --docker-server=sinnet.azurecr.io --docker-username=sinnet --docker-password=<access key> --docker-email=siudeks@gmail.com

To configure expected dynamic properties on CI pipeline, you may use locally script as below:
```bash
export USERVICE_WEBAPI_IMAGE_TAG=latest
export USERVICE_PROJECTS_IMAGE_TAG=latest
export WEBAPP_IMAGE_TAG=latest
export USERVICE_TIMEENTRIES_IMAGE_TAG=latest
export REPORTS_IMAGE_TAG=latest
./makeconfig.sh
```
And later on apply on e.g. dev01
```bash
. initshell.sh local
# to install/upgrade application:
h upgrade --install sinnet-reports . -f config.yaml --create-namespace
# or, just to upgrade:
h upgrade sinnet-reports . -f config.yaml

```

##
## Target kubernetes configuration
- **microk8s with custom ingress**. It is used in some places because ingress in microk8s has defined customized class and namespace
- **[cert-manager](https://cert-manager.io/)** installed with version 1.6
- **create k8s namespace *onlex-sinnet-local* and use it for further local deployment**
- **add position to localhopst hosts file (e.g. C:\Windows\System32\drivers\etc\hosts): 127.0.0.1 sinnet.local** because such domain is expected by ingress in onlex-sinnet-local namespace
- create and self-signed cert (used locally instead of letsencrypt), and import the key to k8s to support secure connection
```bash
openssl req -x509 -nodes -days 365 -newkey rsa:2048 -keyout tls.key -out tls.crt -subj "/CN=sinnet.local/O=sinnet.local"
kubectl create secret tls portal-tls-secret --key tls.key --cert tls.crt -n onlexnet-sinnet-local
```
- next, start postgresql database and load secrets to allow application read secrets and connect to database
```bash
k apply -f app-secrets.yaml
```
