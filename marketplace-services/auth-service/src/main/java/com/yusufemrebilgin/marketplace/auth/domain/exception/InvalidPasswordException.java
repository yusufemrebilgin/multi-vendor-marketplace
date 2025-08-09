package com.yusufemrebilgin.marketplace.auth.domain.exception;

import com.yusufemrebilgin.marketplace.common.domain.exception.DomainValidationException;

public class InvalidPasswordException extends DomainValidationException {
    public InvalidPasswordException(String message) {
        super(message);
    }
}
