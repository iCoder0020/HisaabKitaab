package com.hisaabkitaab.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserReply {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("phone_number")
    @Expose
    private String phone_number;
    @SerializedName("category")
    @Expose
    private String category;


    public int getId(){
        return id;
    }

    public void setId(int id){ this.id = id; }

    public String getEmail(){
        return email;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public String getUserName(){
        return username;
    }

    public void setUserName(String username){
        this.username = username;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

}