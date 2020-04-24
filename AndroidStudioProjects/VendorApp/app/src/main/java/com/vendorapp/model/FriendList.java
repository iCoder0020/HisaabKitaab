package com.vendorapp.model;

import java.util.ArrayList;
import java.util.List;

public class FriendList {

    List<Friend> friendList;

    public FriendList(){
        friendList = new ArrayList<Friend>();
    }

    public void addFriend(int id,
                     String username,
                     double amount)
    {
        friendList.add(new Friend(id, username, amount));
    }

    public boolean checkFriend(int id){
        for(Friend f: friendList){
            if(f.getId() == id){
                return true;
            }
        }
        return false;
    }

    public Friend getFriend(int id){
        for(Friend f: friendList){
            if(f.getId() == id){
                return f;
            }
        }
        return null;
    }

    public List<Friend> getFriendList() {
        return friendList;
    }
}
