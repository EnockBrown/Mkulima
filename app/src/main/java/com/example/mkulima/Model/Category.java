package com.example.mkulima.Model;

public class Category {
   private int Id;
    private String Name;
    private  String Image;
    private String Location;
    private  String Price;

    public Category() {
    }

    public Category(int id, String name, String image, String location, String price) {
        Id = id;
        Name = name;
        Image = image;
        Location = location;
        Price = price;
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

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }
}
