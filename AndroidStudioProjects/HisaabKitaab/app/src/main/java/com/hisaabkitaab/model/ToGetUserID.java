package com.hisaabkitaab.model;

public class ToGetUserID {
    private String type;
    private String username;

    public ToGetUserID(String username){
        this.type = "userID";
        this.username = username;
    }
}
