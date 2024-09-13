package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    private final Connection conn = Util.getConnection();

    public UserDaoJDBCImpl() {

    }

    // Создание таблицы для User(ов) — не должно приводить к исключению, если такая таблица уже существует
    @Override
    public void createUsersTable() {
        String sql = "CREATE TABLE IF NOT EXISTS users (id BIGINT AUTO_INCREMENT,\n" +
                "name VARCHAR (20) NOT NULL,\n" +
                "lastname VARCHAR (25) NOT NULL,\n" +
                "age SMALLINT ,\n" +
                "primary key (id))";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Удаление таблицы User(ов) — не должно приводить к исключению, если таблицы не существует
    @Override
    public void dropUsersTable() {
        String sql = "DROP TABLE IF EXISTS users";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Добавление User в таблицу
    @Override
    public void saveUser(String name, String lastName, byte age) {

        String sql = "INSERT INTO USERS (name, lastname, age) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            conn.setAutoCommit(false);
            ps.setString(1, name);
            ps.setString(2, lastName);
            ps.setByte(3, age);

            ps.executeUpdate();
            System.out.println("Saved User: Name = " + name + ", LastName = " + lastName + ", Age = " + age);
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                conn.rollback();
            } catch (SQLException ex) { // ignore
            }
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) { // ignore
            }
        }
    }

    //Удаление User из таблицы (по id)
    @Override
    public void removeUserById(long id) {
        String sql = "DELETE FROM USERS WHERE ID = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            conn.setAutoCommit(false);
            ps.setLong(1, id);

            ps.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                conn.rollback();
            } catch (SQLException ex) { // ignore
            }
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) { // ignore
            }
        }
    }

    //Получение всех User(ов) из таблицы
    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM USERS";
        try (Statement ps = conn.createStatement()) {
            ResultSet rs = ps.executeQuery(sql);
            while (rs.next()) {
                User u1 = new User();
                u1.setId(rs.getLong("id"));
                u1.setName(rs.getString("name"));
                u1.setLastName(rs.getString("lastname"));
                u1.setAge(rs.getByte("age"));
                users.add(u1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    //Очистка содержания таблицы
    @Override
    public void cleanUsersTable() {
        String sql = "TRUNCATE TABLE USERS";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
