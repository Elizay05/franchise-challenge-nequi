variable "cluster_name" {
  default = "franchises-cluster"
  description = "Nombre asignado al clúster ECS"
  type        = string
}

variable "subnet_ids" {
  description = "Listado de subredes utilizadas por las tareas"
  type        = list(string)
}

variable "ecs_task_sg_id" {
  description = "Id del security group asociado a las tareas ECS"
  type        = string
}

variable "repository_url" {
  description = "Dirección del repositorio en Amazon ECR"
  type        = string
}

variable "db_endpoint" {
  description = "Dirección del servidor de la base de datos"
  type        = string
}

variable "db_password" {
  description = "Clave de acceso a la base de datos"
  type        = string
  sensitive   = true
}

variable "alb_target_group_arn" {
  description = "ARN del target group asociado al balanceador de carga"
  type        = string
}