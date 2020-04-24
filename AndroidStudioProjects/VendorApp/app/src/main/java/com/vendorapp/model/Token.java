package com.vendorapp.model;

public class Token {
    private String Authorization;

    public Token(String token){
        this.Authorization = "Bearer ".concat(token);
    }
}
