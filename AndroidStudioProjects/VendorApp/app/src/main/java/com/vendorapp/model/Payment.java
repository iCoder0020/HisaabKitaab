package com.vendorapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Objects;

public class Payment implements Parcelable {

    private int id;
    private double total_amount;
    private double lended_amount;
    private int borrower;
    private int lender;
    private String type;
    private String description;
    private String lender_username;
    private String borrower_username;
    private String status;

    public Payment(double total_amount,
                   double lended_amount,
                   int borrowerid,
                   int lenderid,
                   String description,
                   String lender_username,
                   String borrower_username
    ) {
        this.id = 0;
        this.type = "create";
        this.total_amount = total_amount;
        this.lended_amount = lended_amount;
        this.borrower = borrowerid;
        this.lender = lenderid;
        this.description = description;
        this.borrower_username = borrower_username;
        this.lender_username = lender_username;
        this.status = "P";
    }

    public int getId() { return this.id; }

    public double getTotalAmount(){
        return this.total_amount;
    }

    public double getLendedAmount(){
        return this.lended_amount;
    }

    public int getBorrowerID(){
        return this.borrower;
    }

    public int getLenderID(){
        return this.lender;
    }

    public String getDescription(){
        return this.description;
    }

    public String getStatus(){
        return this.status;
    }

    public String getLender_username(){
        return this.lender_username;
    }

    public String getBorrower_username(){
        return this.borrower_username;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Payment payment = (Payment) o;
        return this.id == payment.id &&
                Double.compare(this.total_amount, payment.total_amount) == 0 &&
                Double.compare(this.lended_amount, payment.lended_amount) == 0 &&
                this.borrower == payment.borrower &&
                this.lender == payment.lender &&
                Objects.equals(this.type, payment.type) &&
                Objects.equals(this.description, payment.description) &&
                Objects.equals(this.lender_username, payment.lender_username) &&
                Objects.equals(this.borrower_username, payment.borrower_username) &&
                Objects.equals(this.status, payment.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, total_amount, lended_amount, borrower, lender, type, description, lender_username, borrower_username, status);
    }


    protected Payment(Parcel in) {
        id = in.readInt();
        total_amount = in.readDouble();
        lended_amount = in.readDouble();
        borrower = in.readInt();
        lender = in.readInt();
        type = in.readString();
        description = in.readString();
        lender_username = in.readString();
        borrower_username = in.readString();
        status = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeDouble(total_amount);
        dest.writeDouble(lended_amount);
        dest.writeInt(borrower);
        dest.writeInt(lender);
        dest.writeString(type);
        dest.writeString(description);
        dest.writeString(lender_username);
        dest.writeString(borrower_username);
        dest.writeString(status);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Payment> CREATOR = new Parcelable.Creator<Payment>() {
        @Override
        public Payment createFromParcel(Parcel in) {
            return new Payment(in);
        }

        @Override
        public Payment[] newArray(int size) {
            return new Payment[size];
        }
    };
}