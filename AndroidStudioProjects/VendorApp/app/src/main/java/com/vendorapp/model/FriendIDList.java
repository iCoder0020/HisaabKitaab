package com.vendorapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class FriendIDList {

    @SerializedName("friends")
    @Expose
    private List<Integer> friendsList;

    public List<Integer> getFriendsList() {
        return friendsList != null ? friendsList : new ArrayList<>();
    }

}