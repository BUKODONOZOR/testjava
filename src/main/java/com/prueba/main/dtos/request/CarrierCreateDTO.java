package com.prueba.main.dtos.request;

import com.prueba.main.util.enums.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CarrierCreateDTO {
    private String name;
    private String username;
    private String password;
    private Role role ;
}
