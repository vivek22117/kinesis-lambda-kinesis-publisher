###################################################
# Fetch remote state for S3 deployment bucket     #
###################################################
data "terraform_remote_state" "backend" {
  backend = "s3"

  config = {
    profile = var.profile
    bucket ="${var.s3_bucket_prefix}-${var.environment}-${var.default_region}"
    key = "state/${var.environment}/aws/terraform.tfstate"
    region = var.default_region
  }
}

data "terraform_remote_state" "rsvp_lambda_kinesis" {
  backend = "s3"

  config = {
    profile = var.profile
    bucket = "${var.s3_bucket_prefix}-${var.environment}-${var.default_region}"
    key = "state/${var.environment}//lambda/rsvp-lambda-kinesis-db/terraform.tfstate"
    region = var.default_region
  }
}