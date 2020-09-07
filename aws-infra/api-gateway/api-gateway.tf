############################################
#             API Gateway REST API         #
############################################
resource "aws_api_gateway_rest_api" "rsvp_subscriber_api" {
  # The name of the REST API
  name = "RSVPSubscriberAPI"

  description = "REST API to add new RSVP Subscriber's!"

  endpoint_configuration {
    types = ["REGIONAL"]
  }
}


#########################################################################
# API Gateway resource, which is a certain path inside the REST API     #
#########################################################################
resource "aws_api_gateway_resource" "rsvp_subscriber_api_resource" {
  rest_api_id = aws_api_gateway_rest_api.rsvp_subscriber_api.id
  parent_id   = aws_api_gateway_rest_api.rsvp_subscriber_api.root_resource_id

  path_part = var.api_gateway_reminder_path
}

######################################################
#               Enable CORS                          #
######################################################
resource "aws_api_gateway_method" "options_method" {
  rest_api_id   = aws_api_gateway_rest_api.rsvp_subscriber_api.id
  resource_id   = aws_api_gateway_resource.rsvp_subscriber_api_resource.id
  http_method   = "OPTIONS"
  authorization = "NONE"
}

resource "aws_api_gateway_method_response" "options_200" {
  depends_on = [aws_api_gateway_method.options_method]

  rest_api_id   = aws_api_gateway_rest_api.rsvp_subscriber_api.id
  resource_id   = aws_api_gateway_resource.rsvp_subscriber_api_resource.id
  http_method   = aws_api_gateway_method.options_method.http_method
  status_code   = "200"

  response_models = {
    "application/json" = "Empty"
  }
  response_parameters = {
    "method.response.header.Access-Control-Allow-Headers" = true,
    "method.response.header.Access-Control-Allow-Methods" = true,
    "method.response.header.Access-Control-Allow-Origin" = true
  }
}


resource "aws_api_gateway_integration" "options_integration" {
  rest_api_id = aws_api_gateway_rest_api.rsvp_subscriber_api.id
  resource_id = aws_api_gateway_resource.rsvp_subscriber_api_resource.id
  http_method = aws_api_gateway_method.options_method.http_method
  type        = "MOCK"

  request_templates = {
    "application/json" = <<EOF
      {
        "statusCode": 200
        }
    EOF

  }
}

resource "aws_api_gateway_integration_response" "options_integration_response" {
  rest_api_id = aws_api_gateway_rest_api.rsvp_subscriber_api.id
  resource_id = aws_api_gateway_resource.rsvp_subscriber_api_resource.id
  http_method = aws_api_gateway_method.options_method.http_method
  status_code = aws_api_gateway_method_response.options_200.status_code

  response_parameters = {
    "method.response.header.Access-Control-Allow-Headers" = "'Content-Type,X-Amz-Date,Authorization,X-Api-Key,X-Amz-Security-Token'"
    "method.response.header.Access-Control-Allow-Methods" = "'GET,OPTIONS,POST,PUT'"
    "method.response.header.Access-Control-Allow-Origin" = "'*'"
  }
}

