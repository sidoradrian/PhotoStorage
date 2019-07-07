package com.nuadu.rest.model;

import com.nuadu.rest.validator.ValidPassword;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;

@Getter
@Setter
public class UserDto {

    @Email
    private String email;
    @ValidPassword
    private String password;
}
