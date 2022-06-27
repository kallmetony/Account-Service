package com.aaronr92.accountservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Payment by this period was not found!")
public class PaymentNotFoundException extends RuntimeException {
}
