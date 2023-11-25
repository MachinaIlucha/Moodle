package com.illiapinchuk.moodle.service.impl;

import com.illiapinchuk.moodle.exception.CannotWriteToS3Exception;
import com.illiapinchuk.moodle.service.FileUploadService;
import jakarta.annotation.Nonnull;
import java.io.IOException;
import java.io.OutputStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.WritableResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/** Service implementation for uploading and downloading files to/from an AWS S3 bucket. */
@Service
@RequiredArgsConstructor
@Slf4j
public class S3FileUploadServiceImpl implements FileUploadService {

  private final ResourceLoader resourceLoader;

  @Value("${spring.cloud.aws.s3.bucket-name}")
  private String imageBucketName;

  @Override
  public String uploadFile(@Nonnull final MultipartFile file) {
    final var s3Resource = resourceLoader.getResource(imageBucketName + file.getOriginalFilename());
    try (OutputStream outputStream = ((WritableResource) s3Resource).getOutputStream()) {
      outputStream.write(file.getBytes());
      return file.getOriginalFilename();
    } catch (IOException e) {
      throw new CannotWriteToS3Exception("Unable to put the file into the S3 bucket.");
    }
  }

  @Override
  public Resource downloadFile(@Nonnull final String filename) {
    return resourceLoader.getResource(imageBucketName + filename);
  }
}
