package com.vendorapp.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class PaymentList implements Parcelable {

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

    public void addPayment(Payment p){
        this.payments.add(p);
    }

    protected PaymentList(Parcel in) {
        if (in.readByte() == 0x01) {
            payments = new ArrayList<Payment>();
            in.readList(payments, Payment.class.getClassLoader());
        } else {
            payments = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (payments == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(payments);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<PaymentList> CREATOR = new Parcelable.Creator<PaymentList>() {
        @Override
        public PaymentList createFromParcel(Parcel in) {
            return new PaymentList(in);
        }

        @Override
        public PaymentList[] newArray(int size) {
            return new PaymentList[size];
        }
    };
}