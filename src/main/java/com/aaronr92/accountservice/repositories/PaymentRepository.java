package com.aaronr92.accountservice.repositories;

import com.aaronr92.accountservice.entities.Payment;
import org.springframework.data.repository.CrudRepository;

import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends CrudRepository<Payment, Long> {
    List<Payment> findPaymentByEmployeeIgnoreCase(String email);

    Optional<Payment> findPaymentByEmployeeIgnoreCaseAndPeriod(String employee, YearMonth period);
}
