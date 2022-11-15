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