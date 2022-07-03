package com.aaronr92.accountservice.services;

import com.aaronr92.accountservice.entities.Payment;
import com.aaronr92.accountservice.entities.User;
import com.aaronr92.accountservice.exceptions.*;
import com.aaronr92.accountservice.repositories.PaymentRepository;
import com.aaronr92.accountservice.repositories.UserRepository;
import com.aaronr92.accountservice.dto.EmployeePaymentResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.DateTimeException;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private UserRepository userRepository;

    public ResponseEntity<Map<String, String>> uploadPayrolls(List<Payment> payments) {
        validatePayment(payments);
        paymentRepository.saveAll(payments);
        return new ResponseEntity<>(Map.of("status", "Added successfully!"), HttpStatus.OK);
    }

    public ResponseEntity<Map<String, String>> updatePayment(Payment payment) {
        checkEmployeeExistence(payment.getEmployee());
        validateSalary(payment);
        Payment p = paymentRepository
                .findPaymentByEmployeeIgnoreCaseAndPeriod(payment.getEmployee(), payment.getPeriod())
                .orElseThrow(EmployeeNotFoundException::new);
        p.setSalary(payment.getSalary());
        paymentRepository.save(p);
        return new ResponseEntity<>(Map.of("status", "Updated successfully!"), HttpStatus.OK);
    }

    public ResponseEntity getPaymentForPeriod(String period, User user) {
        if (period == null) {
            List<Payment> payments = paymentRepository.findPaymentByEmployeeIgnoreCase(user.getEmail());
            payments.sort(Comparator.comparing(Payment::getPeriod).reversed());
            List<EmployeePaymentResponse> responses = new ArrayList<>();
            for (Payment payment : payments) {
                responses.add(createResponse(payment, user));
            }
            return new ResponseEntity<>(responses, HttpStatus.OK);
        } else {
            Payment payment = paymentRepository.findPaymentByEmployeeIgnoreCaseAndPeriod(user.getEmail(), getYearMonthFromString(period))
                    .orElseThrow(PaymentNotFoundException::new);
            EmployeePaymentResponse response = createResponse(payment, user);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    private EmployeePaymentResponse createResponse(Payment payment, User user) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM-yyyy", Locale.ENGLISH);
        EmployeePaymentResponse response = new EmployeePaymentResponse();
        response.setName(user.getName());
        response.setLastname(user.getLastname());
        response.setPeriod(payment.getPeriod().format(formatter));
        response.setSalary(String.format("%d dollar(s) %d cent(s)", payment.getSalary() / 100, payment.getSalary() % 100));
        return response;
    }

    private void validatePayment(List<Payment> payments) {
        for (Payment payment : payments) {
            validateSalary(payment);
            checkEmployeeExistence(payment.getEmployee());
            checkDuplicatePayment(payment);
        }
    }

    private void validateSalary(Payment payment) {
        if (payment.getSalary() < 0)
            throw new SalaryException();
    }

    private void checkEmployeeExistence(String employee) {
        if (userRepository.findUserByEmailIgnoreCase(employee).isEmpty())
            throw new EmployeeNotFoundException();
    }

    private void checkDuplicatePayment(Payment payment) {
        List<Payment> payments = paymentRepository.findPaymentByEmployeeIgnoreCase(payment.getEmployee());
        if (payments.stream().anyMatch(payment::equals))
            throw new DuplicatePaymentException();
    }

    private YearMonth getYearMonthFromString(String period) {
        YearMonth yearMonth = null;
        try {
            yearMonth = YearMonth.parse(period, DateTimeFormatter.ofPattern("MM-yyyy"));
        } catch (DateTimeException e) {
            throw new WrongPeriodException();
        }
        return yearMonth;
    }
}
