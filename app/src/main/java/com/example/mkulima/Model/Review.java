package com.example.mkulima.Model;

public class Review {

    String phone,review,name,product_name;

    public Review() {
    }

    public Review(String phone, String review, String name, String product_name) {
        this.phone = phone;
        this.review = review;
        this.name = name;
        this.product_name = product_name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }
}
