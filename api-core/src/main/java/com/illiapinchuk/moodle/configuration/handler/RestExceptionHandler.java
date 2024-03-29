package com.illiapinchuk.moodle.configuration.handler;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import com.illiapinchuk.moodle.exception.CannotReadJsonException;
import com.illiapinchuk.moodle.exception.CannotWriteToS3Exception;
import com.illiapinchuk.moodle.exception.InvalidJwtTokenException;
import com.illiapinchuk.moodle.exception.JwtTokenExpiredException;
import com.illiapinchuk.moodle.exception.NotValidInputException;
import com.illiapinchuk.moodle.exception.SubmissionAlreadyGradedException;
import com.illiapinchuk.moodle.exception.UserCantModifyAnotherUserException;
import com.illiapinchuk.moodle.exception.UserDontHaveAccessToResource;
import com.illiapinchuk.moodle.model.entity.ApiError;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Global exception handler for REST controllers.
 *
 * <p>This class is annotated with {@code @ControllerAdvice} to handle exceptions globally in the
 * application. It extends {@code ResponseEntityExceptionHandler} to provide custom exception
 * handling for Spring.
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
@Slf4j
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

  /**
   * Handle HttpMediaTypeNotSupportedException. This one triggers when JSON is invalid as well.
   *
   * @param ex HttpMediaTypeNotSupportedException
   * @param headers HttpHeaders
   * @param status HttpStatus
   * @param request WebRequest
   * @return the ApiError object
   */
  @Override
  protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(
      HttpMediaTypeNotSupportedException ex,
      HttpHeaders headers,
      HttpStatusCode status,
      WebRequest request) {
    final var builder = new StringBuilder();
    builder.append(ex.getContentType());
    builder.append(" media type is not supported. Supported media types are ");
    ex.getSupportedMediaTypes().forEach(t -> builder.append(t).append(", "));
    return buildResponseEntity(
        new ApiError(
            HttpStatus.UNSUPPORTED_MEDIA_TYPE, builder.substring(0, builder.length() - 2), ex));
  }

  /**
   * Handle MethodArgumentNotValidException. Triggered when an object fails @Valid validation.
   *
   * @param ex the MethodArgumentNotValidException that is thrown when @Valid validation fails
   * @param headers HttpHeaders
   * @param status HttpStatus
   * @param request WebRequest
   * @return the ApiError object
   */
  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex,
      HttpHeaders headers,
      HttpStatusCode status,
      WebRequest request) {
    final var apiError = new ApiError(BAD_REQUEST);
    apiError.setMessage("Validation error");
    apiError.addValidationErrors(ex.getBindingResult().getFieldErrors());
    apiError.addValidationError(ex.getBindingResult().getGlobalErrors());
    return buildResponseEntity(apiError);
  }

  /**
   * Handles jakarta.validation.ConstraintViolationException. Thrown when @Validated fails.
   *
   * @param ex the ConstraintViolationException
   * @return the ApiError object
   */
  @ExceptionHandler(ConstraintViolationException.class)
  protected ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex) {
    final var apiError = new ApiError(BAD_REQUEST);
    apiError.setMessage("Validation error");
    apiError.addValidationErrors(ex.getConstraintViolations());
    return buildResponseEntity(apiError);
  }

  /**
   * Handles EntityExistsException. Created to encapsulate errors with more detail than
   * jakarta.persistence.EntityExistsException.
   *
   * @param ex the EntityExistsException
   * @return the ApiError object
   */
  @ExceptionHandler(EntityExistsException.class)
  protected ResponseEntity<Object> handleEntityExists(EntityExistsException ex) {
    final var apiError = new ApiError(BAD_REQUEST);
    apiError.setMessage(ex.getMessage());
    apiError.setDebugMessage(ex.getMessage());
    return buildResponseEntity(apiError);
  }

  /**
   * Handles EntityNotFoundException. Created to encapsulate errors with more detail than
   * jakarta.persistence.EntityNotFoundException.
   *
   * @param ex the EntityNotFoundException
   * @return the ApiError object
   */
  @ExceptionHandler(EntityNotFoundException.class)
  protected ResponseEntity<Object> handleEntityNotFound(EntityNotFoundException ex) {
    final var apiError = new ApiError(NOT_FOUND);
    apiError.setMessage(ex.getMessage());
    apiError.setDebugMessage(ex.getMessage());
    return buildResponseEntity(apiError);
  }

  /**
   * Handles AccessDeniedException.
   *
   * @param ex the AccessDeniedException
   * @return the ApiError object
   */
  @ExceptionHandler({
    AccessDeniedException.class,
    UserCantModifyAnotherUserException.class,
    UserDontHaveAccessToResource.class
  })
  protected ResponseEntity<Object> handleEntityNotFound(RuntimeException ex) {
    final var apiError = new ApiError(FORBIDDEN);
    apiError.setMessage(ex.getMessage());
    apiError.setDebugMessage(ex.getMessage());
    return buildResponseEntity(apiError);
  }

  /**
   * Handles NotValidInputException, SubmissionAlreadyGradedException.
   *
   * @param ex the NotValidInputException, SubmissionAlreadyGradedException
   * @return the ApiError object
   */
  @ExceptionHandler({NotValidInputException.class, SubmissionAlreadyGradedException.class})
  protected ResponseEntity<Object> handleNotValidInput(NotValidInputException ex) {
    final var apiError = new ApiError(BAD_REQUEST);
    apiError.setMessage(ex.getMessage());
    apiError.setDebugMessage(ex.getMessage());
    return buildResponseEntity(apiError);
  }

  /**
   * Handles AuthenticationException exceptions.
   *
   * @param ex the InvalidJwtTokenException, AuthenticationException
   * @return the ApiError object
   */
  @ExceptionHandler({
    InvalidJwtTokenException.class,
    AuthenticationException.class,
    JwtTokenExpiredException.class
  })
  protected ResponseEntity<Object> handleNotValidJwt(RuntimeException ex) {
    final var apiError = new ApiError(UNAUTHORIZED);
    apiError.setMessage(ex.getMessage());
    apiError.setDebugMessage(ex.getMessage());
    return buildResponseEntity(apiError);
  }

  /**
   * Handle HttpMessageNotReadableException. Happens when request JSON is malformed.
   *
   * @param ex HttpMessageNotReadableException
   * @param headers HttpHeaders
   * @param status HttpStatus
   * @param request WebRequest
   * @return the ApiError object
   */
  @Override
  protected ResponseEntity<Object> handleHttpMessageNotReadable(
      HttpMessageNotReadableException ex,
      HttpHeaders headers,
      HttpStatusCode status,
      WebRequest request) {
    final var servletWebRequest = (ServletWebRequest) request;
    log.info(
        "{} to {}",
        servletWebRequest.getHttpMethod(),
        servletWebRequest.getRequest().getServletPath());
    final var error = "Malformed JSON request";
    return buildResponseEntity(new ApiError(BAD_REQUEST, error, ex));
  }

  /**
   * Handle HttpMessageNotWritableException.
   *
   * @param ex HttpMessageNotWritableException
   * @param headers HttpHeaders
   * @param status HttpStatus
   * @param request WebRequest
   * @return the ApiError object
   */
  @Override
  protected ResponseEntity<Object> handleHttpMessageNotWritable(
      HttpMessageNotWritableException ex,
      HttpHeaders headers,
      HttpStatusCode status,
      WebRequest request) {
    final var error = "Error writing JSON output";
    return buildResponseEntity(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, error, ex));
  }

  /**
   * Handle NoHandlerFoundException.
   *
   * @param ex
   * @param headers
   * @param status
   * @param request
   * @return
   */
  @Override
  protected ResponseEntity<Object> handleNoHandlerFoundException(
      NoHandlerFoundException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
    final var apiError = new ApiError(BAD_REQUEST);
    apiError.setMessage(
        String.format(
            "Could not find the %s method for URL %s", ex.getHttpMethod(), ex.getRequestURL()));
    apiError.setDebugMessage(ex.getMessage());
    return buildResponseEntity(apiError);
  }

  /**
   * Handle DataIntegrityViolationException, inspects the cause for different DB causes.
   *
   * @param ex the DataIntegrityViolationException
   * @return the ApiError object
   */
  @ExceptionHandler(DataIntegrityViolationException.class)
  protected ResponseEntity<Object> handleDataIntegrityViolation(
      DataIntegrityViolationException ex) {
    if (ex.getCause() instanceof ConstraintViolationException) {
      return buildResponseEntity(
          new ApiError(HttpStatus.CONFLICT, "Database error", ex.getCause()));
    }
    return buildResponseEntity(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, ex));
  }

  /**
   * Exception handler for CannotWriteToS3Exception and CannotReadJsonException. Handles the runtime
   * exception and returns a ResponseEntity with an appropriate error message.
   *
   * @param ex the runtime exception to handle
   * @return a ResponseEntity containing the error message and HTTP status code
   */
  @ExceptionHandler({CannotWriteToS3Exception.class, CannotReadJsonException.class})
  protected ResponseEntity<Object> handleCannotWriteToS3ExceptionAndCannotReadJsonException(
      RuntimeException ex) {
    return buildResponseEntity(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, ex));
  }

  /**
   * Handle Exception, handle generic Exception.class
   *
   * @param ex the Exception
   * @return the ApiError object
   */
  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  protected ResponseEntity<Object> handleMethodArgumentTypeMismatch(
      MethodArgumentTypeMismatchException ex) {
    final var apiError = new ApiError(BAD_REQUEST);
    apiError.setMessage(
        String.format(
            "The parameter '%s' of value '%s' could not be converted to type '%s'",
            ex.getName(), ex.getValue(), ex.getRequiredType().getSimpleName()));
    apiError.setDebugMessage(ex.getMessage());
    return buildResponseEntity(apiError);
  }

  private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
    return new ResponseEntity<>(apiError, apiError.getStatus());
  }
}
