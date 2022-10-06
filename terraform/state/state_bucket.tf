resource "aws_s3_bucket" "worksheet-state" {
  bucket = "worksheet-tf"

  tags = {
    "Name"    = "project-state-bucket"
    "Project" = "worksheets"
  }
}
