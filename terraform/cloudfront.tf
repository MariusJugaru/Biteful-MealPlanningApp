resource "aws_cloudfront_origin_access_control" "oac" {
    name                              = "biteful-oac"

    origin_access_control_origin_type = "s3"
    signing_behavior                  = "always"
    signing_protocol                 = "sigv4"
}

resource "aws_cloudfront_distribution" "frontend" {
    enabled = true

    origin {
        domain_name = aws_s3_bucket.frontend.bucket_regional_domain_name
        origin_id   = "s3-frontend"

        origin_access_control_id = aws_cloudfront_origin_access_control.oac.id
    }

    default_root_object = "index.html"

    default_cache_behavior {
        target_origin_id       = "s3-frontend"
        viewer_protocol_policy = "redirect-to-https"

        allowed_methods = ["GET", "HEAD", "OPTIONS"]
        cached_methods   = ["GET", "HEAD"]

        forwarded_values {
        query_string = false
        cookies {
            forward = "none"
        }
        }
    }

    restrictions {
        geo_restriction {
        restriction_type = "none"
        }
    }

    viewer_certificate {
        cloudfront_default_certificate = true
    }

    custom_error_response {
        error_code         = 403
        response_code      = 200
        response_page_path = "/index.html"
    }

    custom_error_response {
        error_code         = 404
        response_code      = 200
        response_page_path = "/index.html"
    }
}