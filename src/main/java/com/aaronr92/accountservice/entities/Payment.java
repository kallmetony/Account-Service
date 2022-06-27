package com.aaronr92.accountservice.entities;

import com.aaronr92.accountservice.util.YearMonthDateConverter;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.YearMonth;
import java.util.Objects;

@Entity(name = "payment")
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"employee", "period"})})
@Getter
@Setter
@NoArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @JsonIgnore
    private long id;

    @Column
    private String employee;    // user's email

    @Column
    @Convert(converter = YearMonthDateConverter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-yyyy")
    private YearMonth period;   //<mm-YYYY>

    @Column
    private long salary;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Payment payment = (Payment) o;
        return Objects.equals(employee, payment.employee) && Objects.equals(period, payment.period);
    }

    @Override
    public int hashCode() {
        return Objects.hash(employee, period);
    }
}