package com.illiapinchuk.moodle.common.date;

import java.util.Date;

/** A service interface for retrieving the current date and time in a specific time zone. */
public interface DateService {

  /**
   * Returns the current date and time in the specified time zone.
   *
   * @param timeZoneId the ID of the time zone to use
   * @return the current date and time in the specified time zone
   */
  Date getCurrentZonedDateTime(String timeZoneId);
}
