package com.hisaabkitaab.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PaymentUserReply {

    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("total_amount")
    @Expose
    private float total_amount;
    @SerializedName("timestamp")
    @Expose
    private String timestamp;
    @SerializedName("lended_amount")
    @Expose
    private float lended_amount;
    @SerializedName("lender")
    @Expose
    private int lender;
    @SerializedName("borrower")
    @Expose
    private int borrower;
    @SerializedName("status")
    @Expose
    private char status;


    public String getDescription()
    {
        return this.description;
    }

    public float getTotalAmount()
    {
        return this.total_amount;
    }

    public String getTimestamp()
    {
        return this.timestamp;
    }

    public float getLendedAmount()
    {
        return this.lended_amount;
    }

    public int getBorrower()
    {
        return this.borrower;
    }

    public int getLender()
    {
        return this.lender;
    }

    public char getStatus()
    {
        return this.status;
    }
}