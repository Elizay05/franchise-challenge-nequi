variable "alb_dns_name" {
  description = "Dirección DNS del Load Balancer utilizado en las integraciones con API Gateway"
  type        = string
}

variable "region" {
  description = "Región en la que se despliega la infraestructura en AWS"
  type        = string
  default     = "us-east-1"
}