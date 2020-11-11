package com.example.mkulima.Model;

public class Disease {
    private String descriptions,imageLink,name,prevention_control,signs,type;

    public Disease() {
    }

    public Disease(String descriptions, String imageLink, String name, String prevention_control, String signs, String type) {
        this.descriptions = descriptions;
        this.imageLink = imageLink;
        this.name = name;
        this.prevention_control = prevention_control;
        this.signs = signs;
        this.type = type;
    }

    public String getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(String descriptions) {
        this.descriptions = descriptions;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrevention_control() {
        return prevention_control;
    }

    public void setPrevention_control(String prevention_control) {
        this.prevention_control = prevention_control;
    }

    public String getSigns() {
        return signs;
    }

    public void setSigns(String signs) {
        this.signs = signs;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
