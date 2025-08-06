package com.yusufemrebilgin.marketplace.auth.domain.model;

public enum Role {

    CUSTOMER,

    VENDOR,

    ADMIN,

    SUPER_ADMIN;

    public static Role defaultRole() {
        return Role.CUSTOMER;
    }

}
