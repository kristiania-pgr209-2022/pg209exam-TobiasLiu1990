package no.kristiania.pgr209.iseekyou.database;

import jakarta.inject.Inject;
import no.kristiania.pgr209.iseekyou.User;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDao extends AbstractDao<User, Integer> {

    @Inject
    public UserDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public Integer save(User user) throws SQLException {
        try (var connection = dataSource.getConnection()) {
            String query = "insert into users (full_name, email_address, favorite_color, age) values (?, ?, ?, ?)";
            try (var stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, user.getFullName());
                stmt.setString(2, user.getEmail());
                stmt.setString(3, user.getColor());
                stmt.setInt(4, user.getAge());
                stmt.executeUpdate();
                try (var generatedKeys = stmt.getGeneratedKeys()) {
                    generatedKeys.next();
                    user.setId(generatedKeys.getInt(1));
                    return user.getId();
                }
            }
        }
    }

    public User retrieve(int id) throws SQLException {
        try (var connection = dataSource.getConnection()) {
            String query = "select * from users where user_id = ?";
            try (var stmt = connection.prepareStatement(query)) {
                stmt.setInt(1, id);
                try (var resultSet = stmt.executeQuery()) {
                    if (resultSet.next()) {
                        return mapFromResultSet(resultSet);
                    } else {
                        return null;
                    }
                }
            }
        }
    }

    public void updateUserName(User user) throws SQLException {
        try (var connection = dataSource.getConnection()) {
            String query = "update users set full_name = ? where user_id = ?";
            try (var stmt = connection.prepareStatement(query)) {
                stmt.setString(1, user.getFullName());
                stmt.setInt(2, user.getId());
                stmt.executeUpdate();
            }
        }
    }

    public void updateEmail(User user) throws SQLException {
        try (var connection = dataSource.getConnection()) {
            String query = "update users set email_address = ? where user_id = ?";
            try (var stmt = connection.prepareStatement(query)) {
                stmt.setString(1, user.getEmail());
                stmt.setInt(2, user.getId());
                stmt.executeUpdate();
            }
        }
    }

    public void updateAge(User user) throws SQLException {
        try (var connection = dataSource.getConnection()) {
            String query = "update users set age = ? where user_id = ?";
            try (var stmt = connection.prepareStatement(query)) {
                stmt.setInt(1, user.getAge());
                stmt.setInt(2, user.getId());
                stmt.executeUpdate();
            }
        }
    }

    public void updateFavoriteColor(User user) throws SQLException {
        try (var connection = dataSource.getConnection()) {
            String query = "update users set favorite_color = ? where user_id = ?";
            try (var stmt = connection.prepareStatement(query)) {
                stmt.setString(1, user.getColor());
                stmt.setInt(2, user.getId());
                stmt.executeUpdate();
            }
        }
    }

    public boolean validateUser(User user) {
        return !user.getFullName().equals("") && user.getAge() > 0 && !user.getEmail().equals("") && !user.getColor().equals("");
    }

    public boolean updateUser(User user) throws SQLException {
        if (!validateUser(user)) return false;

        try (var connection = dataSource.getConnection()) {
            String query = "UPDATE users SET full_name = ?, age = ?, email_address = ?, favorite_color = ? WHERE user_id = ?";

            try (var stmt = connection.prepareStatement(query)) {
                stmt.setString(1, user.getFullName());
                stmt.setInt(2, user.getAge());
                stmt.setString(3, user.getEmail());
                stmt.setString(4, user.getColor());
                stmt.setInt(5, user.getId());
                stmt.executeUpdate();
            }
            return true;
        }
    }

    public List<User> retrieveAll() throws SQLException {
        try (var connection = dataSource.getConnection()) {
            String query = "select * from users";
            try (var stmt = connection.prepareStatement(query)) {
                try (var resultSet = stmt.executeQuery()) {
                    List<User> userList = new ArrayList<>();
                    while (resultSet.next()) {
                        userList.add(mapFromResultSet(resultSet));
                    }
                    return userList;
                }
            }
        }
    }

    public List<User> retrieveAllUsersExceptSender(int userId) throws SQLException {
        try (var connection = dataSource.getConnection()) {
            String query = "select * from users where user_id != ?";
            try (var stmt = connection.prepareStatement(query)) {
                stmt.setInt(1, userId);
                try (var resultSet = stmt.executeQuery()) {
                    List<User> userList = new ArrayList<>();
                    while (resultSet.next()) {
                        userList.add(mapFromResultSet(resultSet));
                    }
                    return userList;
                }
            }
        }
    }

    private User mapFromResultSet(ResultSet resultSet) throws SQLException {
        var user = new User();
        user.setId(resultSet.getInt("user_id"));
        user.setFullName(resultSet.getString("full_name"));
        user.setEmail(resultSet.getString("email_address"));
        user.setColor(resultSet.getString("favorite_color"));
        user.setAge(resultSet.getInt("age"));
        return user;
    }
}