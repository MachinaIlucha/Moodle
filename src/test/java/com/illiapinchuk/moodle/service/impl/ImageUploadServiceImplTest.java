package com.illiapinchuk.moodle.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.OutputStream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.WritableResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
class ImageUploadServiceImplTest {

  @Mock private ResourceLoader resourceLoader;

  @Mock private WritableResource s3Resource;

  @InjectMocks private FileUploadServiceImpl fileUploadService;

  @Test
  void uploadImage_Success() throws IOException {
    String imageFileName = "example.jpg";
    MultipartFile image =
        new MockMultipartFile("image", imageFileName, "image/jpeg", new byte[] {1, 2, 3});

    when(resourceLoader.getResource(anyString())).thenReturn(s3Resource);
    OutputStream outputStream = mock(OutputStream.class);
    when(s3Resource.getOutputStream()).thenReturn(outputStream);
    doNothing().when(outputStream).write(image.getBytes());

    String result = fileUploadService.uploadFile(image);

    verify(outputStream).write(new byte[] {1, 2, 3});
    assertEquals(imageFileName, result);
  }

  @Test
  void testDownloadImage() {
    String filename = "example.jpg";
    Resource mockResource = Mockito.mock(Resource.class);
    when(resourceLoader.getResource(anyString())).thenReturn(mockResource);

    Resource downloadedResource = fileUploadService.downloadFile(filename);

    assertEquals(mockResource, downloadedResource);
  }
}
