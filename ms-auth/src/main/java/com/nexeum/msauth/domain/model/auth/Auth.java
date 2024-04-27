package com.nexeum.msauth.domain.model.auth;

import lombok.Data;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.index.Indexed;

@Data
@TypeAlias("auth")
public class Auth {
    private String username;
    @Indexed(unique = true)
    private String email;
    private String password;
}
