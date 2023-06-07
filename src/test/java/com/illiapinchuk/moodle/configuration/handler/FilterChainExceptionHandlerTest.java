package com.illiapinchuk.moodle.configuration.handler;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.servlet.HandlerExceptionResolver;

@ExtendWith(MockitoExtension.class)
class FilterChainExceptionHandlerTest {

  @Mock private MockHttpServletRequest mockHttpServletRequest;
  @Mock private MockHttpServletResponse mockHttpServletResponse;
  @Mock private FilterChain filterChain;
  @Mock private HandlerExceptionResolver resolver;
  @InjectMocks private FilterChainExceptionHandler filterChainExceptionHandler;

  @Test
  void doFilterInternalWhenFilterChainThrowsExceptionShouldCallResolver()
      throws ServletException, IOException {
    final var exceptionMessage = "test exception";
    final var testException = new RuntimeException(exceptionMessage);
    doThrow(testException)
        .when(filterChain)
        .doFilter(mockHttpServletRequest, mockHttpServletResponse);
    filterChainExceptionHandler.doFilterInternal(
        mockHttpServletRequest, mockHttpServletResponse, filterChain);

    verify(resolver, only())
        .resolveException(mockHttpServletRequest, mockHttpServletResponse, null, testException);
  }

  @Test
  void doFilterInternalWhenFilterChainDoesNotThrowExceptionShouldNotCallResolver()
      throws IOException, ServletException {
    doNothing().when(filterChain).doFilter(mockHttpServletRequest, mockHttpServletResponse);
    filterChainExceptionHandler.doFilterInternal(
        mockHttpServletRequest, mockHttpServletResponse, filterChain);

    verify(resolver, never()).resolveException(any(), any(), any(), any());
  }
}
