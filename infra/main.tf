module "application" {
  source = "./module_application"
  application_name=var.application_name
  environment_name=var.environment_name
}

module "resourcegroup" {
  source = "./module_application"
  application_name=var.application_name
  environment_name=var.environment_name
}

# data "azurerm_client_config" "current" {
# #   client_id     = data.azurerm_client_config.current.client_id
# #   client_secret = data.azurerm_client_config.current.
# #   tenant_id     = data.azurerm_client_config.current.tenant_id
# }



# data "azurerm_container_registry" "example" {
#     provider = azurerm.sinnet-production
#     name                = "sinnet"
#     resource_group_name = "sinnet-default-manual"
# }

# resource "azurerm_storage_account" "default" {
#     resource_group_name = azurerm_resource_group.default.name
#     name = "${var.resource_group_name}${var.environment}storage"
#     location = var.location
#     account_tier = "Standard"
#     account_replication_type = "LRS"
# }

