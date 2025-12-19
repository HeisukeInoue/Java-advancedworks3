package com.example.dockerapi.controller;

import com.example.dockerapi.model.User;
import com.example.dockerapi.model.Order;
import com.example.dockerapi.model.UserWithOrders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;


import java.sql.PreparedStatement;
import java.sql.Statement;

@RestController
@RequestMapping("/api")
public class HelloController {

    private static final Logger log = LoggerFactory.getLogger(HelloController.class);

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
                    rs.getString("name"),
                    rs.getInt("quantity"),
                    rs.getInt("userid")
                ),
            order_id
        );
        return order;
    }

    @GetMapping("/presents")
    public int howBigYourLove() {
        return 50000000 + 30000;
    }


    @GetMapping("/users-with-orders")
    public List<UserWithOrders> getUsersWithOrders() {
        String sql = """
            SELECT
                u.id   AS u_id,
                u.name AS u_name,
                u.email AS u_email,
                o.id AS o_id,
                o.name AS o_name,
                o.quantity AS o_quantity,
                o.userid AS o_userid
            FROM users u
            LEFT JOIN orders o
            ON o.userid = u.id
            ORDER BY u.id, o.id
            """;
    
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);
    
        Map<Integer, UserWithOrders> map = new LinkedHashMap<>();
    
        for (Map<String, Object> row : rows) {
            int userId = ((Number) row.get("u_id")).intValue();
    
            UserWithOrders u = map.get(userId);
            if (u == null) {
                u = new UserWithOrders(
                    userId,
                    (String) row.get("u_name"),
                    (String) row.get("u_email")
                );
                map.put(userId, u);
            }
    
            Object orderIdObj = row.get("o_id");
            if (orderIdObj != null) {
                int orderId = ((Number) orderIdObj).intValue();
                String orderName = (String) row.get("o_name");
                int quantity = ((Number) row.get("o_quantity")).intValue();
                int orderUserId = ((Number) row.get("o_userid")).intValue();
    
                u.getOrders().add(new Order(orderId, orderName, quantity, orderUserId));
            }
        }
    
        return new ArrayList<>(map.values());
    }

}
// Test comment Wed Dec 17 11:12:58 JST 2025
// Test from container Wed Dec 17 11:40:07 JST 2025
