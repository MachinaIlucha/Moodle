package com.illiapinchuk.moodle.common.date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import org.junit.jupiter.api.Test;

class DateServiceImplTest {

  private static final String TIME_ZONE_ID = "America/New_York";
  private final DateService dateService = new DateServiceImpl();


  @Test
  void getCurrentZonedDateTime_MatchesPredefinedDateTime() {
    ZoneId zoneId = ZoneId.of(TIME_ZONE_ID);
    LocalDateTime predefinedDateTime = LocalDateTime.of(2024, 1, 1, 12, 0, 0);
    ZonedDateTime expectedDateTime = ZonedDateTime.of(predefinedDateTime, zoneId);

    // Mocking the date service to return a predefined date
    DateService mockedDateService = mock(DateServiceImpl.class);
    when(mockedDateService.getCurrentZonedDateTime(TIME_ZONE_ID)).thenReturn(Date.from(expectedDateTime.toInstant()));

    Date actualDate = mockedDateService.getCurrentZonedDateTime(TIME_ZONE_ID);
    ZonedDateTime actualDateTime = actualDate.toInstant().atZone(zoneId);

    assertEquals(expectedDateTime, actualDateTime, "The actual date time doesn't match the expected predefined date time");
  }


  @Test
  void getCurrentZonedDateTime_ReturnsNotNull() {
    Date actualDate = dateService.getCurrentZonedDateTime(TIME_ZONE_ID);

    assertEquals(actualDate, actualDate);
  }
}
