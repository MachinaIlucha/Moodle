package com.illiapinchuk.moodle.common.validator;

import jakarta.validation.constraints.NotNull;
import java.util.Arrays;
import lombok.experimental.UtilityClass;
import org.springframework.web.multipart.MultipartFile;

/** The ImageValidator class provides utility methods for validating image files. */
@UtilityClass
public class ImageValidator {

  /**
   * Checks if the given file is an image file based on its extension.
   *
   * @param file The MultipartFile representing the image file to validate.
   * @return {@code true} if the file is an image file, {@code false} otherwise.
   */
  public boolean isImageFile(@NotNull MultipartFile file) {
    String extension = getFileExtension(file.getOriginalFilename());
    String[] allowedExtensions = {"jpg", "jpeg", "png", "gif"}; // Allowed photo extensions
    return extension != null
        && Arrays.stream(allowedExtensions).anyMatch(extension::equalsIgnoreCase);
  }

  /**
   * Extracts the file extension from the given filename.
   *
   * @param filename The name of the file.
   * @return The file extension in lowercase, or {@code null} if no extension is found.
   */
  private String getFileExtension(String filename) {
    if (filename != null) {
      int dotIndex = filename.lastIndexOf('.');
      if (dotIndex >= 0 && dotIndex < filename.length() - 1) {
        return filename.substring(dotIndex + 1).toLowerCase();
      }
    }
    return null;
  }
}
