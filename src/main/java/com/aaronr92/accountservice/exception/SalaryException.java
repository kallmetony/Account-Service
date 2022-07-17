package com.aaronr92.accountservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Salary must be non negative!")
public class SalaryException extends RuntimeException {
}
