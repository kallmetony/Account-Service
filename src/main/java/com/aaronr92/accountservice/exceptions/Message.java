package com.aaronr92.accountservice.exceptions;

import lombok.Getter;

@Getter
public enum Message {
    SIGNUP("The password length must be at least 12 chars!"),
    CHECK_PASSWORD("Password length must be 12 chars minimum!");

    private final String message;

    Message(String message) {
        this.message = message;
    }
}
