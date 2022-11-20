package no.kristiania.pgr209.iseekyou.database;

import jakarta.inject.Inject;
import no.kristiania.pgr209.iseekyou.Message;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MessageDao extends AbstractDao<Message, Message> {

    @Inject
    public MessageDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public Message save(Message message) throws SQLException {
        try (var connection = dataSource.getConnection()) {
            String query = "INSERT INTO messages (sender_id, content, conversation_id) VALUES (?, ?, ?) ";
            try (var stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setInt(1, message.getSenderId());
                stmt.setString(2, message.getContent());
                stmt.setInt(3, message.getConversationId());
                stmt.executeUpdate();

                try (var generatedKeys = stmt.getGeneratedKeys()) {
                    generatedKeys.next();
                    message.setId(generatedKeys.getInt(1));
                    return message;
                }
            }
        }
    }

    public List<Message> retrieveAllMessagesByConversationId(int id) throws SQLException {
        try (var connection = dataSource.getConnection()) {
            String query = """
                    SELECT Users.full_name, Messages.created, Messages.content
                    FROM Users
                    JOIN Messages
                        ON Users.user_id = Messages.sender_id
                    WHERE conversation_id = ?
                    ORDER BY created
                    """;
            try (var stmt = connection.prepareStatement(query)) {
                stmt.setInt(1, id);
                try (var resultSet = stmt.executeQuery()) {
                    List<Message> conversationMessages = new ArrayList<>();
                    while (resultSet.next()) {
                        Message message = new Message();
                        message.setSenderName(resultSet.getString("full_name"));
                        message.setContent(resultSet.getString("content"));
                        message.setMessageDate(formatDate(resultSet.getTimestamp("created")));

                        conversationMessages.add(message);
                    }
                    return conversationMessages;
                }
            }
        }
    }

    //Separated this as inline as it became long and unreadable.
    private LocalDateTime formatDate(Timestamp timestamp) {
        return timestamp.toLocalDateTime().withNano(0);
    }
}