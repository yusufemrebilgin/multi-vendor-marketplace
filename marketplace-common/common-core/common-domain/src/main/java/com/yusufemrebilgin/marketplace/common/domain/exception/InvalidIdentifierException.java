package com.yusufemrebilgin.marketplace.common.domain.exception;

public class InvalidIdentifierException extends DomainValidationException {
    public InvalidIdentifierException(String message) {
        super(message);
    }
}
