output "ecr_repository_url" {
  description = "Dirección del repositorio en Amazon ECR"
  value       = module.ecr.ecr_url
}

output "db_endpoint" {
  description = "URL de conexión para la base de datos en RDS"
  value       = module.rds.rds_endpoint
}

output "alb_dns_name" {
  description = "DNS del Application Load Balancer"
  value       = module.alb.alb_dns_name
}

output "ecs_cluster_id" {
  description = "Id único del clúster ECS desplegado"
  value       = module.ecs.ecs_cluster_id
}

output "api_invoke_url" {
  description = "URL pública para consumir el API a través de API Gateway"
  value       = module.api_gateway.api_invoke_url
}