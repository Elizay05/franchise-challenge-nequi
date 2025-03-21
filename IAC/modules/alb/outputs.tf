output "alb_dns_name" {
  value       = aws_lb.alb.dns_name
  description = "Dirección DNS del Load Balancer"

}

output "target_group_arn" {
  value       = aws_lb_target_group.target_group.arn
  description = "ARN del grupo de destino (Target Group)"
}

output "ecs_task_sg_id" {
  value       = aws_security_group.task-ecs-security-group.id
  description = "Identificador del Security Group de ECS"
}