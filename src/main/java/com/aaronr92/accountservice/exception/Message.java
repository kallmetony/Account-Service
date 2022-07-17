package com.aaronr92.accountservice.exception;

import lombok.Getter;

@Getter
public enum Message {
    SIGNUP("The password length must be at least 12 chars!"),
    CHECK_PASSWORD("Password length must be 12 chars minimum!"),
    REMOVE_BREACHED_PASSWORD("The password was successfully deleted!"),
    ADD_BREACHED_PASSWORD("The password was successfully added!");

    private final String message;

    Message(String message) {
        this.message = message;
    }
}
