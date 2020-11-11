package com.example.mkulima.Model;

import java.io.Serializable;

public class Product implements Serializable {
    private int Id;
    private String Name,Image, Description,Price, Location, Date_posted,product_type,phone;

    public Product() {
    }

    public Product(String name, String image, String description, String price, String location, String date_posted, String product_type, String phone) {
        Name = name;
        Image = image;
        Description = description;
        Price = price;
        Location = location;
        Date_posted = date_posted;
        this.product_type = product_type;
        this.phone = phone;
    }

    public Product(int id, String name, String image, String description, String price, String location, String date_posted, String product_type, String phone) {
        Id = id;
        Name = name;
        Image = image;
        Description = description;
        Price = price;
        Location = location;
        Date_posted = date_posted;
        this.product_type = product_type;
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getDate_posted() {
        return Date_posted;
    }

    public void setDate_posted(String date_posted) {
        Date_posted = date_posted;
    }

    public String getProduct_type() {
        return product_type;
    }

    public void setProduct_type(String product_type) {
        this.product_type = product_type;
    }
}

