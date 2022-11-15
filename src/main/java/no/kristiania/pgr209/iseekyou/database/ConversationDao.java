package no.kristiania.pgr209.iseekyou.database;

import jakarta.inject.Inject;
import no.kristiania.pgr209.iseekyou.Conversation;

import javax.sql.DataSource;
import java.sql.*;

public class ConversationDao extends AbstractDao<Conversation>{

    @Inject
    public ConversationDao(DataSource dataSource) {
        super(dataSource);
    }

    //Saves a new conversation and also returns the object.
    public int save(Conversation conversation) throws SQLException {
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

//    public Conversation retrieveLastConversation() throws SQLException {
//        try (var connection = dataSource.getConnection()) {
//            String query = "SELECT MAX(conversation_id) as conversation_id FROM Conversations";
//            try (var stmt = connection.prepareStatement(query)) {
//                try (var resultSet = stmt.executeQuery()) {
//                    if (resultSet.next()) {
//                        Conversation conversation = new Conversation();
//                        conversation.setId(resultSet.getInt("conversation_id"));
//                        return conversation;
//                    }
//                }
//            }
//        }
//        return null;
//    }
}