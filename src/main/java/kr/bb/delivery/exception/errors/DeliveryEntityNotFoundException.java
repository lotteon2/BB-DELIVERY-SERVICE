package kr.bb.delivery.exception.errors;

import kr.bb.delivery.exception.errors.common.ErrorCode;

public class DeliveryEntityNotFoundException extends RuntimeException {
  private static final String message = ErrorCode.DELIVERY_NOT_FOUND.getMessage();

  public DeliveryEntityNotFoundException() {
    super(message);
  }
}
