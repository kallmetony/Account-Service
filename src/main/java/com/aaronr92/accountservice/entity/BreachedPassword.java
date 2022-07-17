package com.aaronr92.accountservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@Entity(name = "breached_passwords")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BreachedPassword {

    @Id
    @GeneratedValue(generator = "mySeq")
    @SequenceGenerator(name = "mySeq", sequenceName = "MY_SEQ", allocationSize = 1)
    @JsonIgnore
    private Long id;

    @Column
    private String password;
}
