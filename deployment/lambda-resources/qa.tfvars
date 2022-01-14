default_region = "us-east-1"

environment = "qa"

kinesis_lambda_kinesis_bucket_key = "kinesis-lambda-kinesis/kinesis-rsvp-publisher.zip"
kinesis_publisher_lambda          = "KinesisRSVPPublisher"
kinesis_publisher_lambda_handler  = "com.ddsolutions.publisher.lambda.RSVPRecordPublisher::processEvent"
lambda_memory                     = 384
lambda_timeout                    = 120