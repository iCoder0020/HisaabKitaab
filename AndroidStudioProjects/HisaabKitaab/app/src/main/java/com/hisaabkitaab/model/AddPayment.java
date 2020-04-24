package com.hisaabkitaab.model;


public class AddPayment {
    private double total_amount;
    private double lended_amount;
    private int borrowerid;
    private int lenderid;
    private String type;
    private String description;
    private String status;

    public AddPayment(double total_amount,
                      double lended_amount,
                      int borrowerid,
                      int lenderid,
                      String description
    ) {
        this.type = "create";
        this.total_amount = total_amount;
        this.lended_amount = lended_amount;
        this.borrowerid = borrowerid;
        this.lenderid = lenderid;
        this.description = description;
        this.status = "P";
    }


}