package com.savijan.pcbuilder;

public class Upload {
    private String mName;
    private String mImageUrl;
    private String description;
    private String price;
    private String category;

    public Upload() {
        //empty constructor needed
    }

    public Upload(String mName, String mImageUrl, String description, String price, String category) {

        if (mName.trim().equals("")) {
            mName = "No Name";
        }

        this.mName = mName;
        this.mImageUrl = mImageUrl;
        this.description = description;
        this.price = price;
        this.category = category;
    }
    public String getName() {
        return mName;
    }
    public void setName(String name) {
        mName = name;
    }
    public String getImageUrl() {
        return mImageUrl;
    }
    public void setImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
    }
    public String getDescription() { return description; }
    public String getPrice() { return price; }
    public void setDescription(String description) {this.description = description; }
    public void setPrice(String price) { this.price = price; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
}