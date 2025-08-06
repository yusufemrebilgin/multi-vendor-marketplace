package com.yusufemrebilgin.marketplace.auth.application.port.out;

import com.yusufemrebilgin.marketplace.auth.domain.model.Password;
import com.yusufemrebilgin.marketplace.auth.domain.model.TokenPair;
import com.yusufemrebilgin.marketplace.auth.domain.model.Username;

public interface KeycloakClient {

    TokenPair login(Username username, Password password);

    String register(KeycloakRegisterCommand command);

}
