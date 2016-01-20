resource "aws_s3_bucket" "elliodr-xti-awspresentation-user-sites" {
  bucket = "elliodr-xti-awspresentation-user-sites"
  acl = "public-read"
  policy = "${file("terraform/policy.json")}"
  force_destroy = true

  website {
    index_document = "index.html"
    error_document = "error.html"
  }
}