package com.illiapinchuk.moodle.common.health;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * S3HealthIndicator is a component that checks the health of an S3 bucket by making a request to
 * the specified bucket URL.
 */
@Component
@Slf4j
public class S3HealthIndicator implements HealthIndicator {

  @Value("${spring.cloud.aws.s3.bucket-url}")
  private String bucketUrl;

  /**
   * Checks the health of the S3 bucket by making a request to the specified bucket URL.
   *
   * @return a Health object representing the health status of the S3 bucket
   */
  @Override
  public Health health() {
    try {
      var response = new RestTemplate().getForEntity(bucketUrl, String.class);
      return response.getStatusCode().is2xxSuccessful()
          ? Health.up().build()
          : Health.down().withDetail("error", "S3 does not accessible.").build();
    } catch (Exception e) {
      log.error("Error checking S3 health", e);
      return Health.down().withDetail("error", "S3 does not accessible.").build();
    }
  }
}
