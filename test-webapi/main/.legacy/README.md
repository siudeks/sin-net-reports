# BDD tests for webapi

## Prerequisits
- In the tests we use some predefined users, already existing in Azure B2C, named:
  - Operator1 - person using the system to write its own timeentries
  - Operator2 - person using the system to write its own timeentries
  - ProjectOwner1 - designed to be used to create its own project
  - ProjectOwner2 - designed to be used to create its own project
  - Hint: all users (and their passwords) are created manually as today we can't create them using Terraform with Azure B2C
- passwords for all users are defined and kept in Azure Vault and available for the test as it is run in context of test service principal

## Start work locally
*Run docker* - it depends what you have installed (e.g. Docker desktop, Rancher Desktop etc.)

``` bash
# Run services # to run services
docker-compose up

# To see µservices
dapr dashboard

# run all µservices as script (or run them individually in new shells) ...
./start-stack.sh # to run application services

 # to create env variables based on KeyVault secrets
. init-vars.sh

# Next
dapr run --app-id ac-tests --dapr-http-port 3500 -- mvn clean test # to run tests using local dapr sidecar
# or just selected
dapr run --app-id ac-tests --dapr-http-port 3500 -- mvn test -Dcucumber.options="--tags @only"

```

And, finally, find results of the test located *target/cucumber-reports.html*

## Some examples
- **mvn clean test -Dcucumber.filter.tags="@"** to run tests marked with tag *@only*

## useful links
- [https://github.com/cucumber/cucumber-jvm](https://github.com/cucumber/cucumber-jvm)
- [GraphQL client](https://hantsy.github.io/blog/2021/consuming-graphql-apis-with-quarkus/)
