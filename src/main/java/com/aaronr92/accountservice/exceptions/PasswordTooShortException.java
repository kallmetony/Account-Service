package com.aaronr92.accountservice.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class PasswordTooShortException extends RuntimeException {
    public PasswordTooShortException(Message message) {
        super(message.getMessage());
    }
}
