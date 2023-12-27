package kr.bb.delivery.exceptionhandler;

import bloomingblooms.errors.DomainException;
import bloomingblooms.response.CommonResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class DeliveryRestControllerAdvice extends ResponseEntityExceptionHandler {

  private static final String DOMAIN_EXCEPTION_MESSAGE = "도메인 오류";

  @ExceptionHandler(DomainException.class)
  public CommonResponse domainException(DomainException e) {
    return CommonResponse.fail(DOMAIN_EXCEPTION_MESSAGE, e.getErrorCode().toString());
  }
}
