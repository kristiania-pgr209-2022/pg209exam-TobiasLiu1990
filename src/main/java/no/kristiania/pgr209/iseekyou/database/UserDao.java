package no.kristiania.pgr209.iseekyou.database;

import jakarta.inject.Inject;
import no.kristiania.pgr209.iseekyou.User;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDao extends AbstractDao<User, Boolean> {

    @Inject
    public UserDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public Boolean save(User user) throws SQLException {
        if (validateUser(user)) {
            try (var connection = dataSource.getConnection()) {
                String query = "INSERT INTO users (full_name, email_address, favorite_color, age) VALUES (?, ?, ?, ?)";
                try (var stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                    stmt.setString(1, user.getFullName());
                    stmt.setString(2, user.getEmail());
                    stmt.setString(3, user.getColor());
                    stmt.setInt(4, user.getAge());
                    stmt.executeUpdate();
                    try (var generatedKeys = stmt.getGeneratedKeys()) {
                        generatedKeys.next();
                        user.setId(generatedKeys.getInt(1));
                    }
                    return true;
                }
            }
        }
        return false;
    }

    public User retrieve(int id) throws SQLException {
        try (var connection = dataSource.getConnection()) {
            String query = "SELECT * FROM users WHERE user_id = ?";
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

    public boolean updateUser(User user) throws SQLException {
        if (validateUser(user)) {
            try (var connection = dataSource.getConnection()) {
                String query = "UPDATE users SET full_name = ?, email_address = ?, age = ?, favorite_color = ? WHERE user_id = ?";
                try (var stmt = connection.prepareStatement(query)) {
                    stmt.setString(1, user.getFullName());
                    stmt.setString(2, user.getEmail());
                    stmt.setInt(3, user.getAge());
                    stmt.setString(4, user.getColor());
                    stmt.setInt(5, user.getId());
                    stmt.executeUpdate();
                }
                return true;
            }
        }
        return false;
    }

    public List<User> retrieveAll() throws SQLException {
        try (var connection = dataSource.getConnection()) {
            String query = "SELECT * FROM users";
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

    public List<String> retrieveAllEmails() throws SQLException {
        try (var connection = dataSource.getConnection()) {
            String query = "SELECT email_address FROM users";
            try (var stmt = connection.prepareStatement(query)) {
                try (var resultSet = stmt.executeQuery()) {
                    List<String> emails = new ArrayList<>();
                    while (resultSet.next()) {
                        emails.add(resultSet.getString("email_address"));
                    }
                    return emails;
                }
            }
        }
    }

    public List<User> retrieveAllUsersExceptSender(int id) throws SQLException {
        try (var connection = dataSource.getConnection()) {
            String query = "SELECT * FROM users WHERE user_id != ?";
            try (var stmt = connection.prepareStatement(query)) {
                stmt.setInt(1, id);
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

    /*
        Check so user fields cant be empty.
        Regex checks so there are at least first name and last name.
        Email regex checks format so there is an @ between email-name and domain.
        Checks so email does not already exist in database.
     */
    public boolean validateUser(User user) throws SQLException {
        String nameRegex = ("[a-zA-Z]+ [a-zA-Z]+( [a-zA-Z]+)*");
        String emailRegex = ("[a-zA-Z0-9]+(\\.[a-zA-Z0-9]+)*@[a-zA-Z0-9]+(\\.*[a-zA-Z0-9]+)*\\.[a-zA-Z]{2,}");
        String existingEmail = "";

        List<String> listOfEmails = retrieveAllEmails();

        for (String email : listOfEmails) {
            if (user.getEmail().equals(email)) {
                existingEmail = email;
            }
        }

        if (!user.getFullName().equals("") && !user.getEmail().equals("") && user.getAge() > 0 && !user.getColor().equals("")) {
            if (user.getFullName().matches(nameRegex) && user.getEmail().matches(emailRegex)) {
                if (!user.getEmail().equals(existingEmail)) {
                    return true;
                }
            }
        }
        return false;
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