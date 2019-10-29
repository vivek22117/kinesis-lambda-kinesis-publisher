#############################
# Global Variables          #
#############################
variable "profile" {
  type = string
  description = "AWS profile name for credentials"
}

variable "default_region" {
  type = string
  description = "AWS region name to deploy resources"
}

variable "environment" {
  type = string
  description = "Environment to deploy"
}




#################################
#  Default Variables            #
#################################
variable "s3_bucket_prefix" {
    type = string
  description = "S3 deployment bucket prefix"
  default = "teamconcept-tfstate"
}


#################################
# Application Variables         #
#################################
variable "db_table_name" {
  type = string
  description = "Name of the table for subscribers"
}

variable "hash_key" {
  type = string
  description = "DynmoDB table hash key"
}

variable "billing_mode" {
  type = string
  description = "DynamoDB Billing mode. Can be PROVISIONED or PAY_PER_REQUEST"
}

variable "db_read_capacity" {
  type = number
  description = "DynamoDB read capacity"
}

variable "db_write_capacity" {
  type = number
  description = "DynamoDB write capacity"
}

variable "enable_encryption" {
  type = bool
  description = "Enable DynamoDB server-side encryption"
}

variable "enable_point_in_time_recovery" {
  type = bool
  description = "Enable DynamoDB point in time recovery"
}

variable "kinesis_lambda_kinesis_bucket_key" {
  type = string
  description = "S3 key to upload deployable zip file"
}

variable "kinesis_publisher_lambda" {
  type = string
  description = "AWS Lambda function name"
}

variable "kinesis_publisher_lambda_handler" {
  type = string
  description = "AWS Lambda handler name"
}

variable "lambda_memory" {
  type = number
  description = "AWS Lambda function memory limit"
}

variable "lambda_timeout" {
  type = number
  description = "AWS Lambda function timeout"
}

####################################
# Local variables                  #
####################################
locals {
  common_tags = {
    owner       = "Vivek"
    team        = "TeamConcept"
    environment = var.environment
  }
}