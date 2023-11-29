package kr.bb.delivery.exception.errors.common;

import java.util.Arrays;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "Validataion failed for args"),
    DELIVERY_NOT_FOUND(HttpStatus.NOT_FOUND, "No matching Delivery found"),
    DELIVERY_ADDRESS_NOT_FOUND(HttpStatus.NOT_FOUND, "No matching Delivery Address found");

    private final HttpStatus code;
    private final String message;

    ErrorCode(HttpStatus code, String message){
        this.code = code;
        this.message = message;
    }

    public static ErrorCode valueOfCode(HttpStatus errorCode){
        return Arrays.stream(values())
                .filter(value -> value.code.equals(errorCode))
                .findAny()
                .orElse(null);
    }

}