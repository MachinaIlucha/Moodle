package com.illiapinchuk.moodle.configuration.metrics;

import com.illiapinchuk.moodle.common.constants.ApplicationConstants;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * A Spring AOP aspect that measures the execution time of repository methods and records the
 * metrics using a MeterRegistry.
 */
@Aspect
@Component
@RequiredArgsConstructor
public class RepositoryTimingAspect {

  private final MeterRegistry meterRegistry;

  /**
   * Measures the execution time of repository methods and records the metrics.
   *
   * @param joinPoint The ProceedingJoinPoint representing the intercepted method.
   * @return The result of the intercepted method.
   * @throws Throwable if an error occurs during method interception.
   */
  @Around("execution(* org.springframework.data.repository.Repository+.*(..))")
  public Object timeRepositoryMethods(@Nonnull final ProceedingJoinPoint joinPoint)
      throws Throwable {
    final var methodName = joinPoint.getSignature().toShortString();
    final var sample = Timer.start(meterRegistry);
    try {
      return joinPoint.proceed();
    } finally {
      sample.stop(
          Timer.builder(ApplicationConstants.Web.Metrics.DB_QUERY_DURATION)
              .description(ApplicationConstants.Web.Metrics.QUERY_DURATION_DESC)
              .tag(
                  ApplicationConstants.Web.Metrics.Tag.REQUEST_REPOSITORY_METHOD_NAME_TAG,
                  methodName)
              .register(meterRegistry));
    }
  }
}
