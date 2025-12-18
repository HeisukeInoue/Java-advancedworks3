package com.example.dockerapi.controller;

import com.example.dockerapi.model.User;
import com.example.dockerapi.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HelloController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/hello")
    public String sayHello() {
        return "Hello, Docker World!";
    }

    @GetMapping("/hoge")
    public String sayHoge() {
        return "hogehogehoge";
    }

    @GetMapping("/check-db")
    public String checkDbConnection() {
        try {
            jdbcTemplate.queryForObject("SELECT 1", Integer.class); // MySQLへの接続確認
            return "Database connection is successful!";
        } catch (Exception e) {
            return "Database connection failed!";
        }
    }

    @GetMapping("/users/{user_id}")
    public User getUserById(@PathVariable int user_id) {
        String sql = """
            SELECT
                id,
                name,
                email
            FROM
                users
            WHERE
                id = ?
            """;
        
        User user = jdbcTemplate.queryForObject(
            sql,
            (rs, rowNum) ->
            new User(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("email")
            ),
            user_id
        );
        return user;
    }

    @GetMapping("/orders/{order_id}")
    public Order getOrderById(@PathVariable int order_id) {
        String sql = """
            SELECT
                id,
                product_name,
                quantity
            FROM
                orders
            WHERE
                id = ?
            """;
        
        Order order = jdbcTemplate.queryForObject(
            sql,
            (rs, rowNum) ->
            new Order(
                rs.getInt("id"),
                rs.getString("product_name"),
                rs.getInt("quantity")
            ),
            order_id
        );
        return order;
    }

    @GetMapping("/presents")
    public int howBigYourLove() {
        return 50000000 + 30000;
    }

}
// Test comment Wed Dec 17 11:12:58 JST 2025
// Test from container Wed Dec 17 11:40:07 JST 2025
