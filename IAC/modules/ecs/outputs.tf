output "ecs_cluster_id" {
  description = "Identificador del clúster ECS desplegado"
  value       = aws_ecs_cluster.franchise_cluster.id
}