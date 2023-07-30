package com.illiapinchuk.moodle.common.date;

import jakarta.annotation.Nonnull;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import org.springframework.stereotype.Service;

/** Implementation of the {@link DateService} interface. */
@Service
public class DateServiceImpl implements DateService {
  /**
   * Returns the current date and time in the specified time zone.
   *
   * @param timeZoneId the ID of the time zone to use
   * @return the current date and time in the specified time zone
   */
  @Override
  public Date getCurrentZonedDateTime(@Nonnull final String timeZoneId) {
    return Date.from(ZonedDateTime.of(LocalDateTime.now(), ZoneId.of(timeZoneId)).toInstant());
  }
}
