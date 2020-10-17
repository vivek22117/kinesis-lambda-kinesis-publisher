output "lambda_arn" {
  value = aws_lambda_function.kinesis_rsvp_lambda_publisher.arn
}

output "dynamo_db_arn" {
  value = aws_dynamodb_table.subscriber_table.arn
}

output "dynamo_db_name" {
  value = aws_dynamodb_table.subscriber_table.name
}


