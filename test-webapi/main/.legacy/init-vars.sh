SUBSCRIPTION_NAME="onlexnet-sinnet-app-stg01"
export TENANT_ID=$(az account show --subscription $SUBSCRIPTION_NAME | jq -r .tenantId)

az account set --subscription $SUBSCRIPTION_NAME
export APPLICATION_ID=$(az keyvault secret show --vault-name "sinnet-stg01" --name "application-id" | jq -r .value)
export OPERATOR1_NAME=$(az keyvault secret show --vault-name "sinnet-stg01" --name "test-operator-1-email" | jq -r .value)
export OPERATOR1_PASSWORD=$(az keyvault secret show --vault-name "sinnet-stg01" --name "test-operator-1-password" | jq -r .value)