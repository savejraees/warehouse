package com.saifi.warehouse.model;

public class MainCatogryModel {

    public String getCategoryNAme() {
        return categoryNAme;
    }

    public void setCategoryNAme(String categoryNAme) {
        this.categoryNAme = categoryNAme;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    String categoryNAme;
    int img;

    public Boolean getColor() {
        return color;
    }

    public void setColor(Boolean color) {
        this.color = color;
    }

    Boolean color=false;
}
