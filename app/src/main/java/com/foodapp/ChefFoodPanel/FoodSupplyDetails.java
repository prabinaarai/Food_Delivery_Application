package com.foodapp.ChefFoodPanel;

public class FoodSupplyDetails {
    private String dishes;
    private String quantity;
    private String price;
    private String description;
    private String imageURL;
    private String uid;
    private String chefId;

    // Default constructor required for calls to DataSnapshot.getValue(FoodSupplyDetails.class)
    public FoodSupplyDetails() {
    }

    // Parameterized constructor
    public FoodSupplyDetails(String dishes, String quantity, String price, String description, String imageURL, String uid, String chefId) {
        this.dishes = dishes;
        this.quantity = quantity;
        this.price = price;
        this.description = description;
        this.imageURL = imageURL;
        this.uid = uid;
        this.chefId = chefId;
    }

    // Getters and Setters

    public String getDishes() {
        return dishes;
    }

    public void setDishes(String dishes) {
        this.dishes = dishes;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getChefId() {
        return chefId;
    }

    public void setChefId(String chefId) {
        this.chefId = chefId;
    }
}
