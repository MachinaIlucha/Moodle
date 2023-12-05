package com.illiapinchuk.moodle.service.business;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

/** The FileUploadService interface provides methods for uploading and downloading files. */
public interface FileUploadService {

  /**
   * Uploads file.
   *
   * @param file The file to upload.
   * @return The key or unique identifier associated with the uploaded file.
   */
  String uploadFile(MultipartFile file);

  /**
   * Downloads a file.
   *
   * @param filename The filename of the image to download.
   * @return File resource.
   */
  Resource downloadFile(String filename);
}
