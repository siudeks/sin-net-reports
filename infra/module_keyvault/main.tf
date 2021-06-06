resource "random_id" "id" {
	  byte_length = 8
}

data "azurerm_client_config" "current" {}

resource "azurerm_key_vault" "example" {
  // name                       = "${var.application_name}_${var.environment_name}_${random_id.id.hex}"
  name                       = "${var.application_name}-${var.environment_name}"
  location                   = var.resourcegroup.location
  resource_group_name        = var.resourcegroup.name
  tenant_id                  = data.azurerm_client_config.current.tenant_id
  sku_name                   = "premium"
  soft_delete_retention_days = 7

  access_policy {
    tenant_id = data.azurerm_client_config.current.tenant_id
    object_id = data.azurerm_client_config.current.object_id

    key_permissions = [
      "create",
      "get"
    ]

    secret_permissions = [
      "set",
      "get",
      "delete",
      "purge",
      "recover"
    ]
  }
}

resource "azurerm_key_vault_secret" "instrumentation-key" {
  name         = "instrumentation-key"
  value        = var.instrumentation_key
  key_vault_id = azurerm_key_vault.example.id
}
