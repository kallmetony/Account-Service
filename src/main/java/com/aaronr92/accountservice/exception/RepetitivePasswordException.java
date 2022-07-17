package com.aaronr92.accountservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "The passwords must be different!")
public class RepetitivePasswordException extends RuntimeException {

}
