package com.aaronr92.accountservice.entity;

import com.aaronr92.accountservice.util.Action;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Event {

    @Id
    @GeneratedValue(generator = "mySeq")
    @SequenceGenerator(name = "mySeq", sequenceName = "MY_SEQ", allocationSize = 1)
    @JsonIgnore
    private long id;

    @Column
    @CreationTimestamp
    private LocalDateTime date;

    @Column
    private Action action;

    @Column
    private String subject;

    @Column
    private String object;

    @Column
    private String path;
}
