module "shared" {
  source                  = "../../shared"
  application_name        = "sinnet"
  environment_name        = "prd01"
  psql_infrauser_name     = var.psql_infrauser_name
  psql_infrauser_password = var.psql_infrauser_password
  sinnet_k8s_host         = var.sinnet_k8s_host
  sinnet_k8s_token        = var.sinnet_k8s_token
  env_subscription_id     = var.onlexnet_sinnet_prd01_subscription_id
  shared_subscription_id  = var.sinnet_prod_subscription_id
}