package no.kristiania.pgr209.ISeekYou.database;

import jakarta.inject.Inject;
import no.kristiania.pgr209.ISeekYou.User;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class UserDao {

    private DataSource dataSource;

    public UserDao(DataSource dataSource){
        this.dataSource = dataSource;
    }

    public List<User> listAll() {
        return null;
    }

    public void save(User user) throws SQLException {
        try(var connection = dataSource.getConnection()){
            String query = "insert into users (full_name, email_address) values (?, ?)";
            try(var stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)){
                stmt.setString(1, user.getFullName());
                stmt.setString(2, user.geteMail());
                stmt.executeUpdate();
                try(var generatedKeys = stmt.getGeneratedKeys()){
                    generatedKeys.next();
                    user.setId(generatedKeys.getInt(1));
                }
            }
        }
    }

    public User retrieve(int id) throws SQLException {
        try(var connection = dataSource.getConnection()){
            String query = "select * from users where id = ?";
            try(var stmt = connection.prepareStatement(query)){
                stmt.setInt(1, id);
                try(var resultSet = stmt.executeQuery()){
                    if(resultSet.next()){
                        return mapFromResultSet(resultSet);
                    } else{
                        return null;
                    }
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