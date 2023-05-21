# SinNet Reports

## Goal
- Small project created for [Sin.Net](http://www.sin.net.pl/)
- My testing sandbox to use modern technlogies

## Business
Project allows register service actions provided for some customers, and produce summarized reports in form of small PDF attachments for invoices..

In scope:
- [x] Integrate login with Azure B2C
- [x] Register time and distance of provided service through Web site
- [x] Define list of clients
- [x] Create PDF attachments per client for given period on demand

## Prepare dev environment
* Use WSL2
* Install VSCode
* Install java 17 using SDKMAN
* install npm using [nvm](https://github.com/nvm-sh/nvm)

## To run locally for tests

- run docker
- run docker-compose (to start database)
- run backend
- run frontend
## Used articles
- [How to export OpenTelemetry to Application Insight](https://docs.dapr.io/operations/monitoring/tracing/open-telemetry-collector-appinsights/)
- https://devhub.io/repos/JrCs-docker-letsencrypt-nginx-proxy-companion
- https://blog.linuxserver.io/2017/11/28/how-to-setup-a-reverse-proxy-with-letsencrypt-ssl-for-all-your-docker-apps/
- https://www.digitalocean.com/community/tutorials/how-to-use-traefik-as-a-reverse-proxy-for-docker-containers-on-ubuntu-16-04
- https://github.com/OfficeDev/office-ui-fabric-react/wiki/Ngrok
- https://codewithhugo.com/docker-compose-local-https/
- https://itnext.io/cluster-recipe-external-proxy-for-kubernetes-ingress-or-docker-compose-ingress-with-haproxy-on-f81e3adee5ef

## Run locally the whole stack
```bash
docker-compose up --build # (to rebuild used initdb modeules)
# docker-compose up to run faster last configuration
```
