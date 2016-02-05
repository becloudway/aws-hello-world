resource "aws_lambda_function" "api-java-lambda" {
  filename = "backend/lambda/java/target/aws-helloworld-lambdajava.jar"
  function_name = "willem1-api-java-lambda"
  role = "${aws_iam_role.api-java-lambda-iam-role.arn}"
  handler = "com.xti.aws.helloworld.lambdajava.HelloWorldRequestHandler"
  runtime = "java8"
  timeout = 60
  memory_size = 320
}

resource "aws_iam_role" "api-java-lambda-iam-role" {
  name = "willem1-api-java-lambda-iam-role"
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

resource "aws_iam_role_policy" "api-java-lambda-iam-role-policy" {
  name = "willem1-api-java-lambda-iam-role-policy"
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