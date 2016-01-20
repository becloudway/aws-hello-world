resource "aws_dynamodb_table" "user-table" {
  name = "elliodr_user_table"
  read_capacity = 1
  write_capacity = 1
  hash_key = "email"
  attribute {
    name = "email"
    type = "S"
  }
  stream_enabled = true
  stream_view_type = "NEW_AND_OLD_IMAGES"
}