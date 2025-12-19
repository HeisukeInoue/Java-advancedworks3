package com.example.dockerapi.repository;

import com.example.dockerapi.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class UserRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public User findById(int id) {
        String sql = """
            SELECT id, name, email
            FROM users
            WHERE id = ?
            """;
        
        return jdbcTemplate.queryForObject(
            sql,
            (rs, rowNum) -> new User(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("email")
            ),
            id
        );
    }

    public List<User> findAll() {
        String sql = "SELECT id, name, email FROM users";
        return jdbcTemplate.query(
            sql,
            (rs, rowNum) -> new User(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("email")
            )
        );
    }

    public User save(User user) {
        String sql = """
            INSERT INTO users (name, email)
            VALUES (?, ?)
            """;

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                sql, Statement.RETURN_GENERATED_KEYS
            );
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            return ps;
        }, keyHolder);

        Number key = keyHolder.getKey();
        int id = (key == null) ? 0 : key.intValue();
        return new User(id, user.getName(), user.getEmail());
    }

    public int update(int id, String name, String email) {
        String sql = """
            UPDATE users
            SET name = ?, email = ?
            WHERE id = ?
            """;
        return jdbcTemplate.update(sql, name, email, id);
    }

    public int delete(int id) {
        String sql = "DELETE FROM users WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }
}