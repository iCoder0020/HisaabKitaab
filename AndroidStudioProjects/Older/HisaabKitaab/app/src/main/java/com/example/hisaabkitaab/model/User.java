package com.example.hisaabkitaab.model;

public class User {
    private int id;
    private String email;
    private String username;
    private String password;
    private String auth_token;


    public User(int id,
                String email,
                String username,
                String password,
                String auth_token
    ) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.password = password;
        this.auth_token = auth_token;
    }


    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getToken(){
        return this.auth_token;
    }

}