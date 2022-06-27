package com.aaronr92.accountservice.util;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RoleOperation {

    @JsonProperty("user")
    private String email;

    private String role;

    private String operation;
}
