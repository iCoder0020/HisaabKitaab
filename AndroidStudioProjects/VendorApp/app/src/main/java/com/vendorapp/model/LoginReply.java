package com.vendorapp.model;

public class LoginReply {
    private String refresh;
    private String access;
    public LoginReply(String refresh, String access){
        this.refresh = refresh;
        this.access = access;
    }
    public String getToken(){
        return this.access;
    }
}
