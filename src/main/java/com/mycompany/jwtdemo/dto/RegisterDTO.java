package com.mycompany.jwtdemo.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RegisterDTO {

    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String password;
    private String role;
}
