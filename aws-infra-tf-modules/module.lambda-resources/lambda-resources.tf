####################################################
#adding the lambda archive to the defined bucket   #
####################################################
resource "aws_s3_object" "kinesis_rsvp_publisher_package" {

  bucket = data.terraform_remote_state.s3_buckets.outputs.artifactory_s3_name
  key    = var.kinesis_lambda_kinesis_bucket_key
  source = "${path.module}/../../KinesisPublisher/target/rsvp-publisher-lambda.zip"
  etag   = filemd5("${path.module}/../../KinesisPublisher/target/rsvp-publisher-lambda.zip")
}

resource "aws_lambda_function" "kinesis_rsvp_lambda_publisher" {
  depends_on = [aws_iam_role.k_lambda_k_role, aws_iam_policy.kinesis_lambda_policy]

  description = "Lambda function to publish RSVP records!"

  function_name = var.kinesis_publisher_lambda
  handler       = var.kinesis_publisher_lambda_handler

  s3_bucket = aws_s3_object.kinesis_rsvp_publisher_package.bucket
  s3_key    = aws_s3_object.kinesis_rsvp_publisher_package.key

  source_code_hash = filebase64sha256("${path.module}/../../KinesisPublisher/target/rsvp-publisher-lambda.zip")

  role = aws_iam_role.k_lambda_k_role.arn

  memory_size = var.lambda_memory
  timeout     = var.lambda_timeout
  runtime     = "java8"

  environment {
    variables = {
      isRunningInLambda = "true",
      environment       = var.environment
      db_table          = data.terraform_remote_state.rsvp_subscriber_api.outputs.dynamo_db_name
    }
  }

  tags = merge(local.common_tags, map("Name", "${var.environment}-rsvp-kinesis-publisher"))
}

resource "aws_lambda_event_source_mapping" "kinesis_lambda_event_mapping" {
  depends_on = [aws_iam_role.k_lambda_k_role, aws_lambda_function.kinesis_rsvp_lambda_publisher]

  batch_size        = 100
  event_source_arn  = data.terraform_remote_state.rsvp_lambda_kinesis.outputs.kinesis_arn
  function_name     = aws_lambda_function.kinesis_rsvp_lambda_publisher.arn
  enabled           = true
  starting_position = "TRIM_HORIZON"
}
