variable "vpc_id" {
  type = string
}

variable "aws_region" {
  type    = string
  default = "us-east-1"
}

variable "subnet_ids" {
  type = list(string)
}

variable "db_password" {
  type        = string
  description = "Contraseña de la base de datos"
}