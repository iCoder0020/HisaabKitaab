package com.hisaabkitaab.model;

import java.util.Objects;

public class Friend {

    private int id;
    private String username;
    private double amount;
    private PaymentList payments;

    public Friend(int id,
                  String username,
                  double amount
    ){
        this.id = id;
        this.username = username;
        this.amount = amount;
        this.payments = new PaymentList();
    }

    public int getId(){
        return this.id;
    }

    public String getUsername(){
        return this.username;
    }

    public PaymentList getPayments(){
        return this.payments;
    }

    public double getAmount(){
        return this.amount;
    }

    public void addPayment(Payment p){
        if(p.getBorrowerID() == this.id){
            this.amount -= p.getLendedAmount();
        }
        else{
            this.amount += p.getLendedAmount();
        }
    }


//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        Friend friend = (Friend) o;
//        return this.username == friend.username &&
//                Double.compare(this.amount, friend.amount) == 0 &&
//                Objects.equals(id, blog.id) &&
//                Objects.equals(author, blog.author) &&
//                Objects.equals(title, blog.title) &&
//                Objects.equals(date, blog.date) &&
//                Objects.equals(image, blog.image) &&
//                Objects.equals(description, blog.description);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(id, author, title, date, image, description, views, rating);
//    }
}
