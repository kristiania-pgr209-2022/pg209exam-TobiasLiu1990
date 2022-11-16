package no.kristiania.pgr209.iseekyou.database;

import jakarta.inject.Inject;
import no.kristiania.pgr209.iseekyou.User;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDao extends AbstractDao<User> {

    @Inject
    public UserDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public int save(User user) throws SQLException {
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

    public List<String> getConversationParticipants(int userId, int conversationId) throws SQLException {
        try (var connection = dataSource.getConnection()) {
            String query = """
                    SELECT DISTINCT(Users.full_name)
                    FROM Users
                    JOIN Conversation_members
                        ON Users.user_id = Conversation_members.user_id
                    JOIN Conversations
                        ON Conversation_members.conversation_id = Conversations.conversation_id
                    WHERE Users.user_id != ? AND conversation_members.conversation_id = ?;
                    """;
            try (var stmt = connection.prepareStatement(query)) {
                stmt.setInt(1, userId);
                stmt.setInt(2, conversationId);
                try (var resultSet = stmt.executeQuery()) {
                    List<String> conversationParticipants = new ArrayList<>();
                    while (resultSet.next()) {
                        conversationParticipants.add(resultSet.getString("full_name"));
                    }
                    return conversationParticipants;
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
