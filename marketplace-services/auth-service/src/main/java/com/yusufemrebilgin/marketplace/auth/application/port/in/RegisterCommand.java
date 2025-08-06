package com.yusufemrebilgin.marketplace.auth.application.port.in;

import com.yusufemrebilgin.marketplace.auth.domain.model.Email;
import com.yusufemrebilgin.marketplace.auth.domain.model.IdentityNumber;
import com.yusufemrebilgin.marketplace.auth.domain.model.Password;
import com.yusufemrebilgin.marketplace.auth.domain.model.Username;

public record RegisterCommand(
        String firstName,
        String lastName,
        Username username,
        Password password,
        Email email,
        IdentityNumber identityNumber
) {}
