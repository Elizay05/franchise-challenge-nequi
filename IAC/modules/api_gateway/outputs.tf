output "api_invoke_url" {
  description = "Endpoint base para consumir el API Gateway"
  value       = "https://${aws_api_gateway_rest_api.api.id}.execute-api.${var.region}.amazonaws.com/dev"
}