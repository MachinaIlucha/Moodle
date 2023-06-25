package com.illiapinchuk.moodle.common.validator;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

class ImageValidatorTest {

  @Test
  void testIsImageFile_WithValidImageExtension_ReturnsTrue() {
    MockMultipartFile file = new MockMultipartFile("file", "image.jpg", "image/jpeg", new byte[0]);

    boolean result = ImageValidator.isImageFile(file);

    assertTrue(result);
  }

  @Test
  void testIsImageFile_WithInvalidImageExtension_ReturnsFalse() {
    MockMultipartFile file = new MockMultipartFile("file", "document.pdf", "application/pdf", new byte[0]);

    boolean result = ImageValidator.isImageFile(file);

    assertFalse(result);
  }

  @Test
  void testIsImageFile_WithEmptyFilename_ReturnsFalse() {
    MockMultipartFile file = new MockMultipartFile("file", "", "image/jpeg", new byte[0]);

    boolean result = ImageValidator.isImageFile(file);

    assertFalse(result);
  }

  @Test
  void testIsImageFile_WithNoExtension_ReturnsFalse() {
    MockMultipartFile file = new MockMultipartFile("file", "image", "image/jpeg", new byte[0]);

    boolean result = ImageValidator.isImageFile(file);

    assertFalse(result);
  }
}
