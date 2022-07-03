package com.aaronr92.accountservice.dto;

import com.aaronr92.accountservice.util.Operation;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.ReadOnlyProperty;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserAccess {

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String user;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Operation operation;

    @ReadOnlyProperty
    private String status;
}
