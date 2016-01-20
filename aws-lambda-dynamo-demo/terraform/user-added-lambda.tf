resource "aws_iam_role" "user-added-lambda-iam-role" {
  name = "elliodr-user-added-lambda-iam-role"
  assume_role_policy = <<EOF
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Action": "sts:AssumeRole",
      "Principal": {
        "Service": "lambda.amazonaws.com"
      },
      "Effect": "Allow",
      "Sid": ""
    }
  ]
}
EOF
}

resource "aws_iam_role_policy" "user-added-lambda-iam-role-policy" {
  name = "elliodr-user-added-lambda-iam-role-policy"
  role = "${aws_iam_role.user-added-lambda-iam-role.id}"
  policy = <<EOF
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Action": [
        "dynamodb:GetRecords",
        "dynamodb:GetShardIterator",
        "dynamodb:DescribeStream",
        "dynamodb:ListStreams"
      ],
      "Effect": "Allow",
      "Resource": "${aws_dynamodb_table.user-table.arn}/*"
    },
    {
      "Action": [
        "logs:CreateLogGroup",
         "logs:CreateLogStream",
         "logs:PutLogEvents"
      ],
      "Effect": "Allow",
      "Resource": "arn:aws:logs:*:*:*"
    },
    {
        "Action": [
            "s3:PutObject"
        ],
        "Effect": "Allow",
        "Resource": "${aws_s3_bucket.elliodr-xti-awspresentation-user-sites.arn}/*"
    },
    {
        "Action": [
            "ses:SendEmail"
        ],
        "Effect": "Allow",
        "Resource": "*"
    }
  ]
}
EOF
}

resource "aws_lambda_function" "user-added-lambda" {
  filename = "target/user-added-lambda.jar"
  function_name = "elliodr-user-added-lambda"
  role = "${aws_iam_role.user-added-lambda-iam-role.arn}"
  handler = "com.xti.awspresentation.demo.UserAddedLambda"
  runtime = "java8"
  timeout = 60
  memory_size = 320
}

//resource "aws_lambda_event_source_mapping" "event_source_mapping" {
//  batch_size = 100
//  event_source_arn = "DYNAMODB_STREAM_ARN_GOES_HERE"
//  enabled = true
//  function_name = "${aws_lambda_function.user-added-lambda.arn}"
//  starting_position = "LATEST"
//}