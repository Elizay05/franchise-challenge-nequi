variable "vpc_id" {
  description = "Id de la VPC en la que se despliega la infraestructura"
  type        = string
}

variable "subnet_ids" {
  type = list(string)
  description = "Lista de subnets asociadas al Load Balancer"
}