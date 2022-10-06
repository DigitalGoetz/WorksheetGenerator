
provider "aws" {
  region  = "us-east-2"
  profile = "new-yoga"
}

terraform {
  required_version = ">= 1.0"

  backend "s3" {
    bucket  = "worksheet-tf"
    key     = "worksheets/terraform.tfstate"
    region  = "us-east-2"
    profile = "new-yoga"
  }

  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 3.69.0"
    }
  }
}
