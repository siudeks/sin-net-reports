resource "kubernetes_namespace" "default" {
  metadata {
    name = local.namespace_name
  }
}

resource "azuredevops_project" "project" {
  name       = "Test Project To Delete using Terraform"
  description        = "Test Project Description"
}