package kr.bb.delivery.exception.errors;

public class OrderServiceOutOfServiceException extends RuntimeException {
  private static String message = "주문 서비스가 정상적으로 작동하지 않습니다.";

  public OrderServiceOutOfServiceException() {
    super(message);
  }
}
