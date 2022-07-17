package com.aaronr92.accountservice.repository;

import com.aaronr92.accountservice.entity.Payment;
import org.springframework.data.repository.CrudRepository;

import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends CrudRepository<Payment, Long> {
    List<Payment> findPaymentByEmployeeIgnoreCase(String email);

    Optional<Payment> findPaymentByEmployeeIgnoreCaseAndPeriod(String employee, YearMonth period);
}
