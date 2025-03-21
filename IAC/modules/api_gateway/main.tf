resource "aws_api_gateway_rest_api" "api" {
  name        = "franchisesApi"
  description = "API Gateway para franquicias"
}

# Crear el recurso para "api"
resource "aws_api_gateway_resource" "api" {
  rest_api_id = aws_api_gateway_rest_api.api.id
  parent_id   = aws_api_gateway_rest_api.api.root_resource_id
  path_part   = "api"
}

# Crear el recurso para "v1"
resource "aws_api_gateway_resource" "v1" {
  rest_api_id = aws_api_gateway_rest_api.api.id
  parent_id   = aws_api_gateway_resource.api.id
  path_part   = "v1"
}

# Crear el recurso para "franchises"
resource "aws_api_gateway_resource" "franchises" {
  rest_api_id = aws_api_gateway_rest_api.api.id
  parent_id   = aws_api_gateway_resource.v1.id
  path_part   = "franchises"
}

# Crear el metodo POST para "franchises"
resource "aws_api_gateway_method" "franchise_post" {
  rest_api_id   = aws_api_gateway_rest_api.api.id
  resource_id   = aws_api_gateway_resource.franchises.id
  http_method   = "POST"
  authorization = "NONE"
}
# Integración con el backend
resource "aws_api_gateway_integration" "franchise_post_integration" {
  rest_api_id             = aws_api_gateway_rest_api.api.id
  resource_id             = aws_api_gateway_resource.franchises.id
  http_method             = aws_api_gateway_method.franchise_post.http_method
  integration_http_method = "POST"
  type                    = "HTTP_PROXY"
  uri                     = "http://${var.alb_dns_name}/api/v1/franchises"
  passthrough_behavior    = "WHEN_NO_MATCH"
}

# Crear el recurso para "{franchiseId}"
resource "aws_api_gateway_resource" "franchise_id" {
  rest_api_id = aws_api_gateway_rest_api.api.id
  parent_id   = aws_api_gateway_resource.franchises.id
  path_part   = "{franchiseId}"
}

# Crear el recurso para "name"
resource "aws_api_gateway_resource" "franchise_name" {
  rest_api_id = aws_api_gateway_rest_api.api.id
  parent_id   = aws_api_gateway_resource.franchise_id.id
  path_part   = "name"
}

# Crear el metodo PUT para "name"
resource "aws_api_gateway_method" "franchise_put_name" {
  rest_api_id   = aws_api_gateway_rest_api.api.id
  resource_id   = aws_api_gateway_resource.franchise_name.id
  http_method   = "PUT"
  authorization = "NONE"

  request_parameters = {
    "method.request.path.franchiseId" = true
  }
}

# Integración con el backend
resource "aws_api_gateway_integration" "franchise_put_name_integration" {
  rest_api_id             = aws_api_gateway_rest_api.api.id
  resource_id             = aws_api_gateway_resource.franchise_name.id
  http_method             = aws_api_gateway_method.franchise_put_name.http_method
  integration_http_method = "PUT"
  type                    = "HTTP_PROXY"
  uri                     = "http://${var.alb_dns_name}/api/v1/franchises/{franchiseId}/name"
  request_parameters = {
    "integration.request.path.franchiseId" = "method.request.path.franchiseId"
  }
  passthrough_behavior = "WHEN_NO_MATCH"
}

# Crear el recurso para "branches"
resource "aws_api_gateway_resource" "branches" {
  rest_api_id = aws_api_gateway_rest_api.api.id
  parent_id   = aws_api_gateway_resource.v1.id
  path_part   = "branches"
}

# Crear el metodo POST para "branches"
resource "aws_api_gateway_method" "branch_post" {
  rest_api_id   = aws_api_gateway_rest_api.api.id
  resource_id   = aws_api_gateway_resource.branches.id
  http_method   = "POST"
  authorization = "NONE"
}
# Integración con el backend
resource "aws_api_gateway_integration" "branch_post_integration" {
  rest_api_id             = aws_api_gateway_rest_api.api.id
  resource_id             = aws_api_gateway_resource.branches.id
  http_method             = aws_api_gateway_method.branch_post.http_method
  integration_http_method = "POST"
  type                    = "HTTP_PROXY"
  uri                     = "http://${var.alb_dns_name}/api/v1/branches"
  passthrough_behavior    = "WHEN_NO_MATCH"
}

# Crear el recurso para "{branchId}"
resource "aws_api_gateway_resource" "branch_id" {
  rest_api_id = aws_api_gateway_rest_api.api.id
  parent_id   = aws_api_gateway_resource.branches.id
  path_part   = "{branchId}"
}

# Crear el recurso para "name"
resource "aws_api_gateway_resource" "branch_name" {
  rest_api_id = aws_api_gateway_rest_api.api.id
  parent_id   = aws_api_gateway_resource.branch_id.id
  path_part   = "name"
}

# Crear el metodo PUT para "name"
resource "aws_api_gateway_method" "branch_put_name" {
  rest_api_id   = aws_api_gateway_rest_api.api.id
  resource_id   = aws_api_gateway_resource.branch_name.id
  http_method   = "PUT"
  authorization = "NONE"

  request_parameters = {
    "method.request.path.branchId" = true
  }
}

# Integración con el backend
resource "aws_api_gateway_integration" "branch_put_name_integration" {
  rest_api_id             = aws_api_gateway_rest_api.api.id
  resource_id             = aws_api_gateway_resource.branch_name.id
  http_method             = aws_api_gateway_method.branch_put_name.http_method
  integration_http_method = "PUT"
  type                    = "HTTP_PROXY"
  uri                     = "http://${var.alb_dns_name}/api/v1/branches/{branchId}/name"
  request_parameters = {
    "integration.request.path.branchId" = "method.request.path.branchId"
  }
  passthrough_behavior = "WHEN_NO_MATCH"
}

# Crear el recurso para "products"
resource "aws_api_gateway_resource" "products" {
  rest_api_id = aws_api_gateway_rest_api.api.id
  parent_id   = aws_api_gateway_resource.v1.id
  path_part   = "products"
}

# Crear el metodo POST para "branches"
resource "aws_api_gateway_method" "products_post" {
  rest_api_id   = aws_api_gateway_rest_api.api.id
  resource_id   = aws_api_gateway_resource.products.id
  http_method   = "POST"
  authorization = "NONE"
}
# Integración con el backend
resource "aws_api_gateway_integration" "products_post_integration" {
  rest_api_id             = aws_api_gateway_rest_api.api.id
  resource_id             = aws_api_gateway_resource.products.id
  http_method             = aws_api_gateway_method.products_post.http_method
  integration_http_method = "POST"
  type                    = "HTTP_PROXY"
  uri                     = "http://${var.alb_dns_name}/api/v1/products"
  passthrough_behavior    = "WHEN_NO_MATCH"
}

# Crear el recurso para "{productId}"
resource "aws_api_gateway_resource" "product_id" {
  rest_api_id = aws_api_gateway_rest_api.api.id
  parent_id   = aws_api_gateway_resource.products.id
  path_part   = "{productId}"
}

# Crear el recurso para "branch"
resource "aws_api_gateway_resource" "product_branch" {
  rest_api_id = aws_api_gateway_rest_api.api.id
  parent_id   = aws_api_gateway_resource.product_id.id
  path_part   = "branch"
}

# Crear el recurso para "{branchId}"
resource "aws_api_gateway_resource" "product_branch_id" {
  rest_api_id = aws_api_gateway_rest_api.api.id
  parent_id   = aws_api_gateway_resource.product_branch.id
  path_part   = "{branchId}"
}

# Crear el metodo DELETE para "products"
resource "aws_api_gateway_method" "product_delete" {
  rest_api_id   = aws_api_gateway_rest_api.api.id
  resource_id   = aws_api_gateway_resource.product_branch_id.id
  http_method   = "DELETE"
  authorization = "NONE"

  request_parameters = {
    "method.request.path.productId" = true
    "method.request.path.branchId"  = true
  }
}

# Integración con el backend
resource "aws_api_gateway_integration" "product_delete_integration" {
  rest_api_id             = aws_api_gateway_rest_api.api.id
  resource_id             = aws_api_gateway_resource.product_branch_id.id
  http_method             = aws_api_gateway_method.product_delete.http_method
  integration_http_method = "DELETE"
  type                    = "HTTP_PROXY"
  uri                     = "http://${var.alb_dns_name}/api/v1/products/{productId}/branch/{branchId}"
  request_parameters = {
    "integration.request.path.productId" = "method.request.path.productId"
    "integration.request.path.branchId"  = "method.request.path.branchId"
  }
  passthrough_behavior = "WHEN_NO_MATCH"
}

resource "aws_api_gateway_resource" "product_branch_stock" {
  rest_api_id = aws_api_gateway_rest_api.api.id
  parent_id   = aws_api_gateway_resource.product_branch_id.id
  path_part   = "stock"
}

resource "aws_api_gateway_method" "product_put_stock" {
  rest_api_id   = aws_api_gateway_rest_api.api.id
  resource_id   = aws_api_gateway_resource.product_branch_stock.id
  http_method   = "PUT"
  authorization = "NONE"

  request_parameters = {
    "method.request.path.productId" = true
    "method.request.path.branchId"  = true
  }
}

resource "aws_api_gateway_integration" "product_put_stock_integration" {
  rest_api_id             = aws_api_gateway_rest_api.api.id
  resource_id             = aws_api_gateway_resource.product_branch_stock.id
  http_method             = aws_api_gateway_method.product_put_stock.http_method
  integration_http_method = "PUT"
  type                    = "HTTP_PROXY"
  uri                     = "http://${var.alb_dns_name}/api/v1/products/{productId}/branch/{branchId}/stock"
  request_parameters = {
    "integration.request.path.productId" = "method.request.path.productId"
    "integration.request.path.branchId"  = "method.request.path.branchId"
  }
  passthrough_behavior = "WHEN_NO_MATCH"
}

# Crear el recurso para "franchise"
resource "aws_api_gateway_resource" "products_franchise" {
  rest_api_id = aws_api_gateway_rest_api.api.id
  parent_id   = aws_api_gateway_resource.products.id
  path_part   = "franchise"
}

# Crear el recurso para "{franchiseId}"
resource "aws_api_gateway_resource" "products_franchise_id" {
  rest_api_id = aws_api_gateway_rest_api.api.id
  parent_id   = aws_api_gateway_resource.products_franchise.id
  path_part   = "{franchiseId}"
}

resource "aws_api_gateway_method" "products_get_stock" {
  rest_api_id   = aws_api_gateway_rest_api.api.id
  resource_id   = aws_api_gateway_resource.products_franchise_id.id
  http_method   = "GET"
  authorization = "NONE"

  request_parameters = {
    "method.request.path.franchiseId" = true
  }
}

resource "aws_api_gateway_integration" "products_get_stock_integration" {
  rest_api_id             = aws_api_gateway_rest_api.api.id
  resource_id             = aws_api_gateway_resource.products_franchise_id.id
  http_method             = aws_api_gateway_method.products_get_stock.http_method
  integration_http_method = "GET"
  type                    = "HTTP_PROXY"
  uri                     = "http://${var.alb_dns_name}/api/v1/products/franchise/{franchiseId}"
  request_parameters = {
    "integration.request.path.franchiseId" = "method.request.path.franchiseId"
  }
  passthrough_behavior = "WHEN_NO_MATCH"
}

# Crear el recurso para "name"
resource "aws_api_gateway_resource" "product_name" {
  rest_api_id = aws_api_gateway_rest_api.api.id
  parent_id   = aws_api_gateway_resource.product_id.id
  path_part   = "name"
}

resource "aws_api_gateway_method" "product_put_name" {
  rest_api_id   = aws_api_gateway_rest_api.api.id
  resource_id   = aws_api_gateway_resource.product_name.id
  http_method   = "PUT"
  authorization = "NONE"

  request_parameters = {
    "method.request.path.productId" = true
  }
}

resource "aws_api_gateway_integration" "product_put_name_integration" {
  rest_api_id             = aws_api_gateway_rest_api.api.id
  resource_id             = aws_api_gateway_resource.product_name.id
  http_method             = aws_api_gateway_method.product_put_name.http_method
  integration_http_method = "PUT"
  type                    = "HTTP_PROXY"
  uri                     = "http://${var.alb_dns_name}/api/v1/products/{productId}/name"
  request_parameters = {
    "integration.request.path.productId" = "method.request.path.productId"
  }
  passthrough_behavior = "WHEN_NO_MATCH"
}


#Despliegue api gateway
resource "aws_api_gateway_deployment" "api_deployment" {
  depends_on = [
    aws_api_gateway_integration.franchise_post_integration,
    aws_api_gateway_integration.franchise_put_name_integration,
    aws_api_gateway_integration.branch_post_integration,
    aws_api_gateway_integration.branch_put_name_integration,
    aws_api_gateway_integration.products_post_integration,
    aws_api_gateway_integration.product_delete_integration,
    aws_api_gateway_integration.product_put_stock_integration,
    aws_api_gateway_integration.products_get_stock_integration,
    aws_api_gateway_integration.product_put_name_integration
  ]
  rest_api_id = aws_api_gateway_rest_api.api.id
  stage_name  = "dev"
}