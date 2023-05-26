package ru.orekhov.SpringSecurity.security_app.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class AuthenticationDTO {
    @NotEmpty(message = "Имя не может быть пустым")
    @Size(min = 2,max = 100,message = "имя должно быть от 2 до 100 символов длиной")
    private String username;

    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
