package no.kristiania.pgr209.iseekyou.database;

import jakarta.inject.Inject;
import no.kristiania.pgr209.iseekyou.Conversation;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ConversationDao extends AbstractDao<Conversation, Integer>{

    @Inject
    public ConversationDao(DataSource dataSource) {
        super(dataSource);
    }

    //Saves a new conversation and also returns the object.
    public Integer save(Conversation conversation) throws SQLException {
        try (var connection = dataSource.getConnection()) {
            String query = "insert into conversations (conversation_title) values (?)";
            try (var stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, conversation.getConversationTitle());
                stmt.executeUpdate();
                try (var generatedKeys = stmt.getGeneratedKeys()) {
                    generatedKeys.next();
                    conversation.setId(generatedKeys.getInt(1));
                    return conversation.getId();
                }
            }
        }
    }

    public List<Conversation> retrieveAllConversationsByUserId(int id) throws SQLException {
        try (var connection = dataSource.getConnection()) {
            String query = """
                    SELECT Conversations.conversation_id, Conversations.conversation_title
                    FROM Conversations
                    JOIN Conversation_members
                        ON Conversations.conversation_id = Conversation_members.conversation_id
                    JOIN Users
                        ON Conversation_members.user_id = Users.user_id
                    where Users.user_id = ?;
                    """;
            try (var stmt = connection.prepareStatement(query)) {
                stmt.setInt(1, id);
                try (var resultSet = stmt.executeQuery()) {
                    List<Conversation> conversations = new ArrayList<>();
                    while (resultSet.next()) {
                        conversations.add(mapFromResultSet(resultSet));
                    }
                    return conversations;
                }
            }
        }
    }

    private Conversation mapFromResultSet(ResultSet resultSet) throws SQLException {
        Conversation conversation = new Conversation();
        conversation.setId(resultSet.getInt("conversation_id"));
        conversation.setConversationTitle(resultSet.getString("conversation_title"));
        return conversation;
    }
}