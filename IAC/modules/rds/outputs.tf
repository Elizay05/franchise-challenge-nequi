output "rds_endpoint" {
  description = "Dirección del endpoint de la instancia RDS"
  value       = aws_db_instance.rds_instance.endpoint
}