package com.prueba.main.dtos.reponse;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AuthRequest {

    private String name;
    private String username;
    private String password;

}
