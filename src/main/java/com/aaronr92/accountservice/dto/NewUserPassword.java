package com.aaronr92.accountservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@NoArgsConstructor
@Getter
@Setter
public class NewUserPassword {

    @Pattern(regexp = ".+(@acme.com)$")
    String email;

    @NotBlank
    @JsonProperty("new_password")
    String newPassword;
}
