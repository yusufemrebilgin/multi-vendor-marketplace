package com.yusufemrebilgin.marketplace.common.domain.exception;

public class InvalidAddressException extends DomainValidationException {
    public InvalidAddressException(String message) {
        super(message);
    }
}
