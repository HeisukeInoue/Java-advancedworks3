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

    //ユーザー一覧の取得
    @GetMapping("/users")
    public List<User> getAllUsers() {
        String sql = """
            SELECT
                *           
            FROM
                users
            """;
        
        return jdbcTemplate.query(
            sql,
            (rs, rowNum) -> new User(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("email")
            )
        );
    }

    /**
     * ユーザー追加
     *
     * POST /api/users
     * body: {"name":"...","email":"..."}
     *
     */

    @PostMapping("/users")
    public User createUser(@RequestBody User request) {
        String sql = """
            INSERT INTO users (name, email)
            VALUES (?, ?)
            """;

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, request.getName());
            ps.setString(2, request.getEmail());
            return ps;
        }, keyHolder);

        Number key = keyHolder.getKey();
        int id = (key == null) ? 0 : key.intValue();

        return new User(id, request.getName(), request.getEmail());
    }

    /**
     * ユーザー更新（POST）
     *
     * POST /api/users/{user_id}
     * body: {"name":"...","email":"..."}
     *
     * - 更新できた場合: 200 + 更新後User
     * - 対象が存在しない場合: 404
     */
    @PostMapping("/users/{user_id}")
    public ResponseEntity<?> updateUserByPath(
        @PathVariable("user_id") int userId,
        @RequestBody User request
    ) {
        String sql = """
            UPDATE users
            SET name = ?, email = ?
            WHERE id = ?
            """;

        int affectedRows = jdbcTemplate.update(sql, request.getName(), request.getEmail(), userId);
        log.info("update users: userId={}, affectedRows={}", userId, affectedRows);
        if (affectedRows == 0) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new User(userId, request.getName(), request.getEmail()));
    }
    
    /**
     * ユーザー更新
     *
     * POST /api/users/{user_iえお
     * body: {"name":"...","email":"..."}
     *
     * - 204で返したい場合: ResponseEntity.noContent()
     * - 更新後のJSONを返したい場合: ResponseEntity.ok(new User(...))
     */
    @PutMapping("/users/{user_id}")
    public ResponseEntity<?> updateUser(
        @PathVariable("user_id") int userId,
        @RequestBody User request
    ) {
        String sql = """
            UPDATE users
            SET name = ?, email = ?
            WHERE id = ?
            """;

        jdbcTemplate.update(sql, request.getName(), request.getEmail(), userId);

        // 204で何も返却しない場合（課題の選択肢1）
        // return ResponseEntity.noContent().build();

        // 更新後のレスポンスを返却する場合（課題の選択肢2）
        return ResponseEntity.ok(new User(userId, request.getName(), request.getEmail()));
    }

    /**
     * ユーザー削除
     *
     * DELETE /api/users/{user_id}
     *
     * - 削除できた場合: 200
     * - 対象が存在しない場合: 404
     */
    @DeleteMapping("/users/{user_id}")
    public ResponseEntity<?> deleteUser(@PathVariable("user_id") int userId) {
        String sql = """
            DELETE FROM users
            WHERE id = ?
            """;

        int affectedRows = jdbcTemplate.update(sql, userId);
        if (affectedRows == 0) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
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
