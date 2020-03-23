package com.example.hisaabkitaab.model;

public class Login {
    private String username;
    private String password;
    private String type;

    public Login(String username, String password){
        this.username = username;
        this.password = password;
        this.type = "login";
    }
}