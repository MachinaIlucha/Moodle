package com.illiapinchuk.moodle.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

/** The ImageUploadService interface provides methods for uploading and downloading images. */
public interface ImageUploadService {

  /**
   * Uploads an image file.
   *
   * @param image The image file to upload.
   * @return The key or unique identifier associated with the uploaded image.
   */
  String uploadImage(MultipartFile image);

  /**
   * Downloads an image file.
   *
   * @param filename The filename of the image to download.
   * @return Image resource.
   */
  Resource downloadImage(String filename);
}
