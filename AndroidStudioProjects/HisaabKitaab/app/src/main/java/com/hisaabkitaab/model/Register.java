package com.hisaabkitaab.model;

public class Register {
    private String name;
    private String email;
    private String username;
    private String password;
    private String type;
    private String category;

    public Register(String name,
                String email,
                String username,
                String password
    ) {
        this.name = name;
        this.email = email;
        this.username = username;
        this.password = password;
        this.type = "create";
        this.category = "S";
    }
}