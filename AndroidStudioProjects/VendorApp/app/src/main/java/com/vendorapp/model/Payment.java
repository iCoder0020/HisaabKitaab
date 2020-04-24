package com.vendorapp.model;

public class Payment {

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
}
