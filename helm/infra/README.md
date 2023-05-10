# Dev notes
Install/upgrade
```bash
helm upgrade --install onlexnet-infra . -f values.yaml --create-namespace --namespace onlexnet-infra
```

# Optionally: upgrade ingress in case you would like to upgrade ingress: 
# https://helm.nginx.com/
helm repo add nginx-stable https://helm.nginx.com/stable
helm pull nginx-stable/nginx-ingress
# now adjust files and configuration to new version


## Optionally: upgrade OpenTelemetry chart
- *helm repo update*
- *helm pull open-telemetry/opentelemetry-collector*
- move just downloaded file to *charts* subfolder
- update *Charts.yaml* to use new file
- More: https://github.com/open-telemetry/opentelemetry-helm-charts/tree/main/charts/opentelemetry-collector


## Optionally: upgrade Dapr Helm chart.
helm repo add dapr https://dapr.github.io/helm-charts/
helm repo update
helm pull dapr/dapr
# See which chart versions are available
helm search repo dapr --devel --versions



# cert-manager
# update when required to the latest version
# https://cert-manager.io/docs/installation/helm/
helm repo add jetstack https://charts.jetstack.io
helm pull jetstack/cert-manager


# OpenTelemetry
helm repo add open-telemetry https://open-telemetry.github.io/opentelemetry-helm-charts
helm repo update
helm pull open-telemetry/opentelemetry-operator
To see configuration: helm show values open-telemetry/opentelemetry-operator


https://github.com/jaegertracing/helm-charts
helm repo add jaegertracing https://jaegertracing.github.io/helm-charts
helm pull jaegertracing/jaeger



# view Jeager UI
minikube service onlexnet-infra-jaeger-query --url -n onlexnet-infra
or
kubectl port-forward service/onlexnet-infra-jaeger-query 16686:16686 -n onlexnet-infra
and then http://localhost:16686

# view dapr dashboard
minikube service dapr-dashboard --url -n onlexnet-infra
