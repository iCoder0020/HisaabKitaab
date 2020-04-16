package com.hisaabkitaab.model;

public class PostReply {
    private int code;
    private String message;

    public PostReply(int code, String message){
        this.code = code;
        this.message = message;
    }

    public String getData(){
        String rep = String.valueOf(code).concat(" "+message);
        return rep;
    }

}
