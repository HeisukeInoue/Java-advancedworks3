package com.example.dockerapi.model;

public class Order {

    private int id;
    private String name;
    private int quantity;
    private int userid;

    // デフォルトコンストラクタ
    public Order() {
    }

    // 全フィールドのコンストラクタ
    public Order(int id, String name, int quantity, int userid) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.userid = userid;
    }

    // Getter と Setter
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }
}