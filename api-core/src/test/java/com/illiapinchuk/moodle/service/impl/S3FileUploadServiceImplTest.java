package com.illiapinchuk.moodle.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.illiapinchuk.moodle.common.TestConstants;
import com.illiapinchuk.moodle.exception.CannotWriteToS3Exception;
import java.io.IOException;
import java.io.OutputStream;

import com.illiapinchuk.moodle.service.impl.business.S3FileUploadServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.WritableResource;
import org.springframework.web.multipart.MultipartFile;

import static org.mockito.Mockito.any;

@ExtendWith(MockitoExtension.class)
class S3FileUploadServiceImplTest {

  @Mock private ResourceLoader resourceLoader;
  @Mock private WritableResource s3Resource;
  @InjectMocks private S3FileUploadServiceImpl fileUploadService;

  @Test
  void uploadImage_Success() throws IOException {
    final var outputStream = mock(OutputStream.class);

    when(resourceLoader.getResource(anyString())).thenReturn(s3Resource);
    when(s3Resource.getOutputStream()).thenReturn(outputStream);
    doNothing().when(outputStream).write(TestConstants.FileConstants.VALID_FILE.getBytes());

    final var result = fileUploadService.uploadFile(TestConstants.FileConstants.VALID_FILE);

    verify(outputStream).write(new byte[] {1, 2, 3});
    assertEquals(TestConstants.FileConstants.VALID_FILENAME, result);
  }

  @Test
  void uploadFile_shouldThrowCannotWriteToS3ExceptionWhenIOExceptionOccurs() throws IOException {
    final var file = mock(MultipartFile.class);
    final var resource = mock(WritableResource.class);
    final var outputStream = mock(OutputStream.class);

    when(file.getOriginalFilename()).thenReturn("example.txt");
    when(file.getBytes()).thenReturn("Test content".getBytes());
    when(resourceLoader.getResource(anyString())).thenReturn(resource);
    when(resource.getOutputStream()).thenReturn(outputStream);
    doThrow(IOException.class).when(outputStream).write(any(byte[].class));

    assertThrows(CannotWriteToS3Exception.class, () -> fileUploadService.uploadFile(file));
  }

  @Test
  void downloadFile_shouldReturnResource() {
    final var expectedResource = mock(Resource.class);
    when(resourceLoader.getResource(anyString())).thenReturn(expectedResource);

    final var result = fileUploadService.downloadFile(TestConstants.FileConstants.VALID_FILENAME);

    assertEquals(expectedResource, result);
  }
}
