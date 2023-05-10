# Content
Folder contains codebase of client part of SinNet PSA application.

- **npm install** install packages
- **npm run generate** generate local graphql models
- **npm start** to run against loal backend

## Push latest image for local development
```bash`
eval $(minikube docker-env)
npm run build
docker build -t sinnet.azurecr.io/webapp:latest ..
```

## Used articles
* https://daveceddia.com/pluggable-slots-in-react-components/
* https://www.develop1.net/public/post/2020/05/11/pcf-detailslist-layout-with-fluent-ui
* https://reactgo.com/react-hooks-apollo/
* https://docs.microsoft.com/en-us/azure/active-directory/develop/reference-app-manifest
* https://github.com/syncweek-react-aad/react-aad/tree/master/samples/react-typescript
