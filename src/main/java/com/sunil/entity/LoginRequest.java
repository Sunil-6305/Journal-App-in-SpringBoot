package com.sunil.entity;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    @JsonAlias({"username", "UserName", "userName"}) // ✅ Accept any spelling
    private String userName;

    @JsonAlias({"password", "Password"})
    private String password;
}
