package com.example.dockerapi.model;

public class User {
    private int id;
    private String name;
    private String email;

    // デフォルトコンストラクタ
    public User() {
    }

    // 全フィールドのコンストラクタ
    public User(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

