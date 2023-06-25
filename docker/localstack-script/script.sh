#!/bin/bash
awslocal s3api create-bucket \
--bucket s3-image-bucket \
--create-bucket-configuration LocationConstraint=eu-central-1