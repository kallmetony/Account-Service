package com.aaronr92.accountservice.util;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class EmployeePaymentResponse {

    private String name;

    private String lastname;

    private String period;

    private String salary;
}
