package no.kristiania.pgr209.iseekyou.database;

import jakarta.inject.Inject;
import no.kristiania.pgr209.iseekyou.Conversation;
import no.kristiania.pgr209.iseekyou.ConversationMembers;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ConversationMembersDao extends AbstractDao<ConversationMembers> {

    @Inject
    public ConversationMembersDao(DataSource dataSource) {
        super(dataSource);
    }

    public int save(ConversationMembers recipients) throws SQLException {
        try (var connection = dataSource.getConnection()) {
            String query = "INSERT INTO conversation_members (user_id, conversation_id) VALUES (?, ?)";
            try (var stmt = connection.prepareStatement(query)) {
                stmt.setInt(1, recipients.getRecipientId());
                stmt.setInt(2, recipients.getConversationId());
                stmt.executeUpdate();
                return recipients.getConversationId();
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
                        Conversation conversation = new Conversation();
                        conversation.setId(resultSet.getInt("conversation_id"));
                        conversation.setConversationTitle(resultSet.getString("conversation_title"));
                        conversations.add(conversation);
                    }
                    return conversations;
                }
            }
        }
    }
}