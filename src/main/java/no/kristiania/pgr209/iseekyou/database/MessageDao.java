package no.kristiania.pgr209.iseekyou.database;

import jakarta.inject.Inject;
import no.kristiania.pgr209.iseekyou.Message;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MessageDao {

    private final DataSource dataSource;

    @Inject
    public MessageDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void save(Message message, String fullName, int conversationId) throws SQLException {
        try (var connection = dataSource.getConnection()) {
            String query = "insert into messages (sender_id, date, content, conversation_id) values (?, ?, ?, ?) ";
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
//            String query = "select * from messages where conversation_id = ? order by date desc";
            String query = """
                    SELECT Users.full_name, Messages.message_id, Messages.sender_id, Messages.date, Messages.content
                    FROM Users
                    JOIN Messages
                        ON Users.user_id = Messages.sender_id
                    WHERE conversation_id = ?
                    ORDER BY date DESC
                    """;
            try (var stmt = connection.prepareStatement(query)) {
                stmt.setInt(1, id);
                try (var resultSet = stmt.executeQuery()) {
                    List<Message> conversationMessages = new ArrayList<>();
                    while (resultSet.next()) {
                        Message message = new Message();
//                        message.setId(resultSet.getInt("message_id"));
                        message.setSenderName(resultSet.getString("full_name"));
//                        message.setSender(resultSet.getInt("sender_id"));
                        message.setMessageText(resultSet.getString("content"));
                        message.setMessageDate(resultSet.getDate("date"));
                        conversationMessages.add(message);
                    }
                    return conversationMessages;
                }
            }
        }
    }
}


