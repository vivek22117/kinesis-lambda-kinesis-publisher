#############################
# Global Variables          #
#############################
variable "default_region" {
  type        = string
  description = "AWS region name to deploy resources"
}

variable "environment" {
  type        = string
  description = "Environment to deploy"
}


#################################
# Application Variables         #
#################################
variable "kinesis_lambda_kinesis_bucket_key" {
  type        = string
  description = "S3 key to upload deployable zip file"
}

variable "kinesis_publisher_lambda" {
  type        = string
  description = "AWS Lambda function name"
}

variable "kinesis_publisher_lambda_handler" {
  type        = string
  description = "AWS Lambda handler name"
}

variable "lambda_memory" {
  type        = number
  description = "AWS Lambda function memory limit"
}

variable "lambda_timeout" {
  type        = number
  description = "AWS Lambda function timeout"
}

####################################
# Local variables                  #
####################################
locals {
  common_tags = {
    owner       = "Vivek"
    team        = "DD-Team"
    environment = var.environment
  }
}