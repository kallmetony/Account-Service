package com.aaronr92.accountservice.controllers;

import com.aaronr92.accountservice.entities.Payment;
import com.aaronr92.accountservice.entities.User;
import com.aaronr92.accountservice.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class PaymentController {

    @Autowired
    PaymentService paymentService;

    @GetMapping("/empl/payment")
    ResponseEntity getPayment(@AuthenticationPrincipal User user,
                              @RequestParam(required = false) String period) {
        return paymentService.getPaymentForPeriod(period, user);
    }

    @PostMapping("/acct/payments")
    ResponseEntity<Map<String, String>> uploadPayrolls(@RequestBody List<Payment> payments) {
        return paymentService.uploadPayrolls(payments);
    }

    @PutMapping("/acct/payments")
    ResponseEntity<Map<String, String>> updatePaymentInfo(@RequestBody Payment payment) {
        return paymentService.updatePayment(payment);
    }
}
