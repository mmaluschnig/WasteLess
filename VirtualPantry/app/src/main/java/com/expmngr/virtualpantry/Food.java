package com.expmngr.virtualpantry;

public class Food {
    private int id;
    private String name;
    private Integer quantity;
    private String category;
    private float time_till_expiry;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public float getTime_till_expiry() {
        return time_till_expiry;
    }

    public void setTime_till_expiry(float time_till_expiry) {
        this.time_till_expiry = time_till_expiry;
    }
}
