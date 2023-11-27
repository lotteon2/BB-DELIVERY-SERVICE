package kr.bb.delivery.exceptionhandler;

import bloomingblooms.errors.DomainException;
import bloomingblooms.response.ErrorResponse;
import kr.bb.delivery.exception.errors.common.ErrorCode;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class DeliveryRestControllerAdvice extends ResponseEntityExceptionHandler {

  private static final String DOMAIN_EXCEPTION_MESSAGE = "도메인 오류";

  @NotNull
  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      @NotNull MethodArgumentNotValidException ex,
      @NotNull HttpHeaders headers,
      @NotNull HttpStatus status,
      @NotNull WebRequest request) {
    ErrorCode validationError = ErrorCode.VALIDATION_ERROR;
    return ResponseEntity.status(validationError.getCode())
        .body(
            ErrorResponse.builder()
                .code(String.valueOf(validationError.getCode()))
                .message(validationError.getMessage()));
  }

  @ExceptionHandler(DomainException.class)
  public ResponseEntity<ErrorResponse> domainException(DomainException e) {
    int statusCode = e.getStatusCode();

    ErrorResponse body =
        ErrorResponse.builder()
            .code(String.valueOf(statusCode))
            .message(DOMAIN_EXCEPTION_MESSAGE)
            .build();

    return ResponseEntity.status(statusCode).body(body);
  }
}
