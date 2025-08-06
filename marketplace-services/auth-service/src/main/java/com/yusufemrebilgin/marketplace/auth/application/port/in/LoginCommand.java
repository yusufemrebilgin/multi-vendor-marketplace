package com.yusufemrebilgin.marketplace.auth.application.port.in;

import com.yusufemrebilgin.marketplace.auth.domain.model.Password;
import com.yusufemrebilgin.marketplace.auth.domain.model.Username;

public record LoginCommand(Username username, Password password) {}
