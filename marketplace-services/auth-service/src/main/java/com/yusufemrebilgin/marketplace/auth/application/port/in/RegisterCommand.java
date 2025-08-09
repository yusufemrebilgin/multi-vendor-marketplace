package com.yusufemrebilgin.marketplace.auth.application.port.in;

import com.yusufemrebilgin.marketplace.auth.domain.model.Password;
import com.yusufemrebilgin.marketplace.auth.domain.model.Username;
import com.yusufemrebilgin.marketplace.common.domain.model.Email;
import com.yusufemrebilgin.marketplace.common.domain.model.IdentityNumber;

public record RegisterCommand(
        String firstName,
        String lastName,
        Username username,
        Password password,
        Email email,
        IdentityNumber identityNumber
) {}
