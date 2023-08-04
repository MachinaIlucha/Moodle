package com.illiapinchuk.moodle.common.date;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class DateServiceImplTest {

  private final DateServiceImpl dateService = new DateServiceImpl();

  @Test
  void getCurrentZonedDateTime_WithValidTimeZone_ReturnsCurrentDateAndTime() {
    final var timeZoneId = "America/New_York";

    final var result = dateService.getCurrentZonedDateTime(timeZoneId);

    assertNotNull(result);
  }

  @Test
  void getCurrentZonedDateTime_WithNullTimeZone_ThrowsException() {
    final String timeZoneId = null;

    assertThrows(NullPointerException.class, () -> dateService.getCurrentZonedDateTime(timeZoneId));
  }

  @Test
  void getCurrentZonedDateTime_WithInvalidTimeZone_ThrowsException() {
    final var timeZoneId = "Invalid_Time_Zone";

    assertThrows(Exception.class, () -> dateService.getCurrentZonedDateTime(timeZoneId));
  }
}
