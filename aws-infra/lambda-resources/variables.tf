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

variable "enable_streams" {
  type = bool
  description = "Enable DynamoDB streams"
}

variable "stream_view_type" {
  type        = string
  description = "When an item in the table is modified, what information is written to the stream KEYS_ONLY, NEW_IMAGE, OLD_IMAGE, NEW_AND_OLD_IMAGES."
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