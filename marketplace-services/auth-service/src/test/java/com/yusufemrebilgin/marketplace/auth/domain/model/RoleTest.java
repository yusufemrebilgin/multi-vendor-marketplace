package com.yusufemrebilgin.marketplace.auth.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RoleTest {

    @Test
    @DisplayName("Should return 'CUSTOMER' as default role")
    void shouldReturnCustomerAsDefault() {
        Role defaultRole = Role.defaultRole();
        assertEquals(Role.CUSTOMER, defaultRole);
    }

}