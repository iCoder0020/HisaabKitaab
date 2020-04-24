package com.vendorapp.model;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class PaymentList {

    private List<Payment> payments;

    public PaymentList(){
        payments = new ArrayList<Payment>();
    }

    public List<Payment> getPayments() {
        return payments != null ? payments : new ArrayList<>();
    }

    public void addPayments(List<Payment> newpayments) {
        Log.w("PaymentList", "here");
        Log.w("PaymentList Size: ", Integer.toString(newpayments.size()));

        for(Payment p : newpayments){
            Log.w("PaymentList Desc: ", p.getDescription());
            this.payments.add(p);
        }
    }
}