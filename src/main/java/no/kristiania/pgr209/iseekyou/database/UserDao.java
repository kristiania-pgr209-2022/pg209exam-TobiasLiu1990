package no.kristiania.pgr209.iseekyou.database;

import no.kristiania.pgr209.iseekyou.User;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserDao extends AbstractDao<User> {

    public UserDao(DataSource dataSource) {
        super(dataSource);
    }

    public int save(User user) throws SQLException {
        try (var connection = dataSource.getConnection()) {
            String query = "insert into users (full_name, email_address, favorite_color) values (?, ?, ?)";
            try (var stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, user.getFullName());
                stmt.setString(2, user.getEmail());
                stmt.setString(3, user.getColor().toString());
                stmt.executeUpdate();
                try (var generatedKeys = stmt.getGeneratedKeys()) {
                    generatedKeys.next();
                    user.setId(generatedKeys.getInt(1));
                    return user;
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

    public void updateUserName(User user, int id) throws SQLException {
        String username = user.getFullName();
        try (var connection = dataSource.getConnection()) {
            String query = "update users set full_name = ? where user_id = ?";
            try (var stmt = connection.prepareStatement(query)) {
                stmt.setString(1, username);
                stmt.setInt(2, id);
                stmt.executeUpdate();
            }
        }
    }

    public void updateEmail(User user, int id) throws SQLException {
        String email = user.getEmail();
        try (var connection = dataSource.getConnection()) {
            String query = "update users set email_address = ? where user_id = ?";
            try (var stmt = connection.prepareStatement(query)) {
                stmt.setString(1, email);
                stmt.setInt(2, id);
                stmt.executeUpdate();
            }
        }
    }

    public void updateFavoriteColor(User user, int id) throws SQLException {
        String favoriteColor = user.getColor();
        try (var connection = dataSource.getConnection()) {
            String query = "update users set favorite_color = ? where user_id = ?";
            try (var stmt = connection.prepareStatement(query)) {
                stmt.setString(1, favoriteColor);
                stmt.setInt(2, id);
                stmt.executeUpdate();
            }
        }
    }

    public List<User> listAll() throws SQLException {
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

    private User mapFromResultSet(ResultSet resultSet) throws SQLException {
        var user = new User();
        user.setId(resultSet.getInt("user_id"));
        user.setFullName(resultSet.getString("full_name"));
        user.setEmail(resultSet.getString("email_address"));
        user.setColor(resultSet.getString("favorite_color"));
//        UserColor color = UserColor.valueOf(resultSet.getString("favorite_color").toUpperCase());
//        user.setColor(color);
        return user;
    }


    public List<User> getAllUsersExceptSender(int userId) throws SQLException {
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
}

























