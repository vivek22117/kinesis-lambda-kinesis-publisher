############################################################
#        Kinesis Lambda Module Implementation              #
############################################################
module "kinesis_lambda_kinesis" {
  source = "../../aws-infra-tf-modules/module.lambda-resources"

  default_region = var.default_region
  environment = var.environment


  kinesis_lambda_kinesis_bucket_key = var.kinesis_lambda_kinesis_bucket_key
  kinesis_publisher_lambda = var.kinesis_publisher_lambda
  kinesis_publisher_lambda_handler = var.kinesis_publisher_lambda_handler
  lambda_memory = var.lambda_memory
  lambda_timeout = var.lambda_timeout
}
