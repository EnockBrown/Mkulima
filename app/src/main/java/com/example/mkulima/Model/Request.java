package com.example.mkulima.Model;

public class Request {
    private String date_posted,description,phone,location,name,type,userName;

    public Request() {
    }

    public Request(String date_posted, String description, String phone, String location, String name, String type, String userName) {
        this.date_posted = date_posted;
        this.description = description;
        this.phone = phone;
        this.location = location;
        this.name = name;
        this.type = type;
        this.userName = userName;
    }

    public String getDate_posted() {
        return date_posted;
    }

    public void setDate_posted(String date_posted) {
        this.date_posted = date_posted;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
