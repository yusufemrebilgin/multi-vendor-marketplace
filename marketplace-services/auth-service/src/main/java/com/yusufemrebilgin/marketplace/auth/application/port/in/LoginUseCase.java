package com.yusufemrebilgin.marketplace.auth.application.port.in;

import com.yusufemrebilgin.marketplace.auth.domain.model.TokenPair;

public interface LoginUseCase {

    TokenPair login(LoginCommand command);

}
