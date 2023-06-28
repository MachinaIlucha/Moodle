package com.illiapinchuk.moodle.configuration.metrics;

import com.illiapinchuk.moodle.common.constants.ApplicationConstants;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * A Spring OncePerRequestFilter that filters incoming requests and records metrics using a
 * MeterRegistry.
 */
@Component
@RequiredArgsConstructor
public class MetricFilter extends OncePerRequestFilter {

  private final MeterRegistry meterRegistry;

  /**
   * Filters the incoming request, records request metrics, and forwards the request to the next
   * filter in the chain.
   *
   * @param request The HttpServletRequest object representing the incoming request.
   * @param response The HttpServletResponse object representing the response.
   * @param filterChain The FilterChain object for invoking the next filter in the chain.
   * @throws ServletException if a servlet error occurs.
   * @throws IOException if an I/O error occurs.
   */
  @Override
  protected void doFilterInternal(
      @NotNull final HttpServletRequest request,
      @NotNull final HttpServletResponse response,
      @NotNull final FilterChain filterChain)
      throws ServletException, IOException {
    final var uri = request.getRequestURI();
    final var sample = Timer.start(meterRegistry);
    try {
      filterChain.doFilter(request, response);
    } finally {
      Counter.builder(ApplicationConstants.Web.Metrics.APP_REQUESTS_TOTAL)
          .description(ApplicationConstants.Web.Metrics.TOTAL_REQUESTS_DESC)
          .tag(ApplicationConstants.Web.Metrics.Tag.REQUEST_URI_TAG, uri)
          .register(meterRegistry)
          .increment();

      sample.stop(
          Timer.builder(ApplicationConstants.Web.Metrics.APP_REQUEST_DURATION)
              .description(ApplicationConstants.Web.Metrics.REQUEST_DURATION_DESC)
              .tag(ApplicationConstants.Web.Metrics.Tag.REQUEST_URI_TAG, uri)
              .register(meterRegistry));
    }
  }
}
