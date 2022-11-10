package no.kristiania.pgr209.iseekyou.database;

import no.kristiania.pgr209.iseekyou.Message;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MessageDao {

    private DataSource dataSource;

    public MessageDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void save(Message message, String fullName, int conversationId) throws SQLException {
        try (var connection = dataSource.getConnection()) {
            String query = "insert into messages (sender, date, content, conversation_id) values (?, ?, ?, ?) ";
            try (var stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, fullName);
                stmt.setDate(2, message.getMessageDate());
                stmt.setString(3, message.getMessageText());
                stmt.setInt(4, conversationId);
                stmt.executeUpdate();

                try (var generatedKeys = stmt.getGeneratedKeys()) {
                    generatedKeys.next();
                    message.setId(generatedKeys.getInt(1));
                }
            }
        }
    }

    public List<Message> retrieveAllMessagesByConversationId(int id) throws SQLException {
        try (var connection = dataSource.getConnection()) {
            String query = "select * from messages where conversation_id = ? order by date desc";
            try (var stmt = connection.prepareStatement(query)) {
                stmt.setInt(1, id);
                try (var resultSet = stmt.executeQuery()) {
                    List<Message> conversationMessages = new ArrayList<>();
                    while (resultSet.next()) {
                        Message message = new Message();
                        message.setId(resultSet.getInt(1));
                        message.setMessageText(resultSet.getString(2));
                        message.setMessageDate(resultSet.getDate(3));
                        conversationMessages.add(message);
                    }
                    return conversationMessages;
                }
            }
        }
    }
}


