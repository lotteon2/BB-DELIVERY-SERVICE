package kr.bb.delivery.exception.errors;

import kr.bb.delivery.exception.errors.common.ErrorCode;

public class DeliveryAddressEntityNotFoundException extends RuntimeException {
  private static final String message = ErrorCode.DELIVERY_ADDRESS_NOT_FOUND.getMessage();

  public DeliveryAddressEntityNotFoundException() {
    super(message);
  }
}
