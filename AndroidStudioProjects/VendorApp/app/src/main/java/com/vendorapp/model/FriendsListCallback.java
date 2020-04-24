package com.vendorapp.model;

import java.util.List;

public interface FriendsListCallback {
    void onSuccess(List<Payment> payments);
    void onError();
}