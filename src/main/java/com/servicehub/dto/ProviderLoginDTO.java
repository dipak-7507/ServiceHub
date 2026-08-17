package com.servicehub.dto;

public class ProviderLoginDTO {

    private String email;
    private String password;

    public ProviderLoginDTO(){

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
