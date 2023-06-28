package com.illiapinchuk.moodle.configuration.metrics;

import io.micrometer.cloudwatch2.CloudWatchConfig;
import io.micrometer.cloudwatch2.CloudWatchMeterRegistry;
import io.micrometer.core.instrument.Clock;
import io.micrometer.core.instrument.MeterRegistry;
import java.net.URI;
import java.time.Duration;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cloudwatch.CloudWatchAsyncClient;

/** Configuration class for setting up CloudWatch metrics in Spring Boot. */
@Configuration
public class MetricsConfiguration {

  @Value("${spring.cloud.aws.credentials.access-key}")
  private String awsAccessKey;

  @Value("${spring.cloud.aws.credentials.secret-key}")
  private String awsSecretKey;

  @Value("${spring.cloud.aws.cloudwatch.uri}")
  private String cloudWatchURI;

  /**
   * Creates and configures a CloudWatchAsyncClient bean.
   *
   * @return CloudWatchAsyncClient instance
   */
  @Bean
  public CloudWatchAsyncClient cloudWatchAsyncClient() {
    return CloudWatchAsyncClient.builder()
        .region(Region.EU_CENTRAL_1)
        .credentialsProvider(
            StaticCredentialsProvider.create(
                AwsBasicCredentials.create(awsAccessKey, awsSecretKey)))
        .endpointOverride(URI.create(cloudWatchURI))
        .build();
  }

  /**
   * Creates and configures a MeterRegistry bean for CloudWatch.
   *
   * @param cloudWatchConfig CloudWatch configuration
   * @param cloudWatchAsyncClient CloudWatch async client
   * @return MeterRegistry instance
   */
  @Bean
  public MeterRegistry getMeterRegistry(
      CloudWatchConfig cloudWatchConfig, CloudWatchAsyncClient cloudWatchAsyncClient) {
    return new CloudWatchMeterRegistry(cloudWatchConfig, Clock.SYSTEM, cloudWatchAsyncClient);
  }

  /**
   * Creates and configures a CloudWatchConfig bean for CloudWatch.
   *
   * @return CloudWatchConfig instance
   */
  @Bean
  public CloudWatchConfig cloudWatchConfig() {
    return new MoodleCloudWatchConfig();
  }

  /** Custom implementation of CloudWatchConfig specific to Moodle application. */
  private static class MoodleCloudWatchConfig implements CloudWatchConfig {
    private final Map<String, String> configuration =
        Map.of(
            "cloudwatch.namespace", "moodle", "cloudwatch.step", Duration.ofMinutes(1).toString());

    @Override
    public String get(String key) {
      return configuration.get(key);
    }
  }
}
