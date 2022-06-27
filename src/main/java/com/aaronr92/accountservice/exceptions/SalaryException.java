package com.aaronr92.accountservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Salary must be non negative!")
public class SalaryException extends RuntimeException {
}
