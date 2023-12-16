package kr.bb.delivery.exceptionhandler;

import bloomingblooms.errors.DomainException;
import bloomingblooms.response.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class DeliveryRestControllerAdvice extends ResponseEntityExceptionHandler {

  private static final String DOMAIN_EXCEPTION_MESSAGE = "도메인 오류";

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
