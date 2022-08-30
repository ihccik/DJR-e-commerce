package com.company;

import com.company.category.Category;

import javax.sound.midi.Soundbank;
import java.util.UUID;

public class Product {

    private UUID id;
    private String name;
    private Double price;
    private Integer stock;
    private Integer remainingStock;
    private UUID categoryId;

    public Product(UUID id, String name, Double price, Integer stock, Integer remainingStock, UUID categoryId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.remainingStock = remainingStock;
        this.categoryId = categoryId;
    }


    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Double getPrice() {
        return price;
    }

    public Integer getStock() {
        return stock;
    }

    public Integer getRemainingStock() {
        return remainingStock;
    }

    public void setRemainingStock(Integer remainingStock) {
        this.remainingStock = remainingStock;
    }

    public UUID getCategoryId() {
        return categoryId;
    }

    public String getCategoryName() throws Exception {

        for (Category category : StaticConstants.CATEGORY_LIST) {
            if (getCategoryId().toString().equals(category.getId().toString())) {
                return category.getName();
            }
        }
        throw new Exception("Category not found," + getName());

    }

    /*public String getDeliveryDueDate() throws Exception {
        for (Category category : StaticConstants.CATEGORY_LIST) {
            if (getCategoryId().toString().equals(category.getId().toString())) {
                return category.findDeliveryDueDate();
            }
        }
        throw new Exception("Category could not find");
    }*/
    public Object getDeliveryDueDate()  {

        try {
            for (Category category : StaticConstants.CATEGORY_LIST) {
                if (getCategoryId().toString().equals(category.getId().toString())) {
                    return category.findDeliveryDueDate();
                }
            }
        } catch (Exception e) {
            e.getMessage();
        }
        return "Category not found";
    }

    @Override
    public String toString() {

            return "Product name: " + name +"\n"+
                    "id: " + id +"\n"+
                    "price: " + price +"\n"+
                    "stock: " + stock +"\n"+
                    "remainingStock: " + remainingStock +"\n"+
                    "categoryId: " + categoryId +"\n"+
                    "delivery ETA: "+getDeliveryDueDate()+"\n";
    }

    public static void listProducts() {
        StaticConstants.PRODUCT_LIST.forEach(System.out::println);
    }




}












