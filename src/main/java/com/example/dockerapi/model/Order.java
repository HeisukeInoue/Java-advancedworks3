package com.example.dockerapi.model;

public class Order {

    private int id;
    private String product_name;
    private int quantity;

    // デフォルトコンストラクタ
    public Order() {
    }

    // 全フィールドのコンストラクタ
    public Order(int id, String product_name, int quantity) {
        this.id = id;
        this.product_name = product_name;
        this.quantity = quantity;
    }

    // Getter と Setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return product_name;
    }

    public void setName(String product_name) {
        this.product_name = product_name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

}
