package com.example.mkulima.Model;

public class User {

    private String Name,Email,Password,Farmer_type,Country,Gender,Age_group,Tele_farmer,Farm_run_loc,Farm_loc,phone;
    private int id;

    public User() {
    }

    public User(String name, String email, String password, String farmer_type, String country, String gender, String age_group, String tele_farmer, String farm_run_loc, String farm_loc, String phone, int id) {
        Name = name;
        Email = email;
        Password = password;
        Farmer_type = farmer_type;
        Country = country;
        Gender = gender;
        Age_group = age_group;
        Tele_farmer = tele_farmer;
        Farm_run_loc = farm_run_loc;
        Farm_loc = farm_loc;
        this.phone = phone;
        this.id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getFarmer_type() {
        return Farmer_type;
    }

    public void setFarmer_type(String farmer_type) {
        Farmer_type = farmer_type;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getAge_group() {
        return Age_group;
    }

    public void setAge_group(String age_group) {
        Age_group = age_group;
    }

    public String getTele_farmer() {
        return Tele_farmer;
    }

    public void setTele_farmer(String tele_farmer) {
        Tele_farmer = tele_farmer;
    }

    public String getFarm_run_loc() {
        return Farm_run_loc;
    }

    public void setFarm_run_loc(String farm_run_loc) {
        Farm_run_loc = farm_run_loc;
    }

    public String getFarm_loc() {
        return Farm_loc;
    }

    public void setFarm_loc(String farm_loc) {
        Farm_loc = farm_loc;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
