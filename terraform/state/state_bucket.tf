resource "aws_s3_bucket" "worksheet-state" {
  bucket        = "worksheet-tf"
  force_destroy = true
  tags = {
    "Name"    = "project-state-bucket"
    "Project" = "worksheets"
  }
}
