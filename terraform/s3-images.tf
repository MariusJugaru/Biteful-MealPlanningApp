# Images bucket
resource "aws_s3_bucket" "bucket" {
    bucket = "biteful-images"
}

resource "aws_s3_bucket_public_access_block" "block" {
    bucket = aws_s3_bucket.bucket.id

    block_public_acls = false
    block_public_policy = false
    ignore_public_acls = false
    restrict_public_buckets = false
}

resource "aws_s3_bucket_policy" "public_read" {
    bucket = aws_s3_bucket.bucket.id

    policy = jsonencode({
        "Version": "2012-10-17",
        "Statement": [
            {
                "Sid": "PublicReadGetObject",
                "Effect": "Allow",
                "Principal": "*",
                "Action": "s3:GetObject",
                "Resource": "${aws_s3_bucket.bucket.arn}/*"
            }
        ]
    })
}