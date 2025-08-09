package com.yusufemrebilgin.marketplace.common.domain.model;

import com.yusufemrebilgin.marketplace.common.domain.exception.InvalidFullNameException;

/**
 * Requires a non-empty first name, last name is optional. <p>
 * Throws {@link InvalidFullNameException} if the first name is invalid.
 */
public record FullName(String firstName, String lastName) {

    public FullName {
        if (firstName == null || firstName.trim().isEmpty()) {
            throw new InvalidFullNameException("First name cannot be null or empty");
        }

        firstName = firstName.trim();

        if (lastName != null) {
            lastName = lastName.trim();
            if (lastName.isEmpty()) {
                lastName = null;
            }
        }
    }

    @Override
    @SuppressWarnings("all")
    public String toString() {
        return (lastName != null) ? firstName + " " + lastName : firstName;
    }

}
