package com.yusufemrebilgin.marketplace.auth.domain.exception;

import com.yusufemrebilgin.marketplace.common.domain.exception.DomainValidationException;

public class InvalidUsernameException extends DomainValidationException {
    public InvalidUsernameException(String message) {
        super(message);
    }
}
