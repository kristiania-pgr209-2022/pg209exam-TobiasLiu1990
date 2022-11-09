package no.kristiania.pgr209.iseekyou.database;

import jakarta.inject.Inject;
import no.kristiania.pgr209.iseekyou.User;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserDao {

    private final DataSource dataSource;

    @Inject
    public UserDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void save(User user) throws SQLException {
        try (var connection = dataSource.getConnection()) {
            String query = "insert into users (full_name, email_address) values (?, ?)";
            try (var stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, user.getFullName());
                stmt.setString(2, user.geteMail());
                stmt.executeUpdate();
                try (var generatedKeys = stmt.getGeneratedKeys()) {
                    generatedKeys.next();
                    user.setId(generatedKeys.getInt(1));
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
        user.seteMail(resultSet.getString("email_address"));
        return user;
    }
}