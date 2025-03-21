module "ecr" {
  source = "./modules/ecr"
}
terraform {
  backend "s3" {
    bucket  = "franchises-tf-state-bucket-626635408488"
    key     = "terraform.tfstate"
    region  = "us-east-1"
    encrypt = true
  }
}
module "ecs" {
  source               = "./modules/ecs"
  cluster_name         = "Franchise-cluster-tf"
  subnet_ids           = var.subnet_ids
  ecs_task_sg_id       = module.alb.ecs_task_sg_id
  repository_url       = module.ecr.ecr_url
  db_endpoint          = module.rds.rds_endpoint
  db_password          = var.db_password
  alb_target_group_arn = module.alb.target_group_arn
}


module "alb" {
  source     = "./modules/alb"
  vpc_id     = var.vpc_id
  subnet_ids = var.subnet_ids
}

module "api_gateway" {
  source       = "./modules/api_gateway"
  alb_dns_name = module.alb.alb_dns_name
}

module "rds" {
  vpc_id      = var.vpc_id
  source      = "./modules/rds"
  db_password = var.db_password
}