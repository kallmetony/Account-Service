package com.aaronr92.accountservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Employee not found!")
public class EmployeeNotFoundException extends RuntimeException {
}
