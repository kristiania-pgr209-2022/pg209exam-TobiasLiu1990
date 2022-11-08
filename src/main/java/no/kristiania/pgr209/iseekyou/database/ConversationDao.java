package no.kristiania.pgr209.iseekyou.database;

import no.kristiania.pgr209.iseekyou.Conversation;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ConversationDao {

    private DataSource dataSource;

    public ConversationDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void save(Conversation conversation) throws SQLException {
        try (var connection = dataSource.getConnection()) {
            String query = "insert into conversation (conversation_name) values (?)";
            try (var stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, conversation.getConversationTitle());
                stmt.executeUpdate();
                try (var generatedKeys = stmt.getGeneratedKeys()) {
                    generatedKeys.next();
                    conversation.setId(generatedKeys.getInt(1));
                }
            }
        }
    }

    public List<Conversation> retrieveAllConversationsByUserId(int id) throws SQLException {
        try (var connection = dataSource.getConnection()) {
            String query = """
                       select me.content, me.date, me.sender, co.conversation_name
                       from conversation_members as come
                       join conversation as co
                        on co.conversation_id = come.conversation_id
                       join message as me
                        on co.conversation_id = me.conversation_id
                       where come.user_id = ?;
                    """;
            try (var stmt = connection.prepareStatement(query)) {
                stmt.setInt(1, id);
                try (var resultSet = stmt.executeQuery()) {
                    List<Conversation> allConversations = new ArrayList<>();
                    while (resultSet.next()) {
                        Conversation conversation = new Conversation();
                        conversation.setId(resultSet.getInt(1));
                        conversation.setConversationTitle(resultSet.getString(2));
                        allConversations.add(conversation);
                    }
                    return allConversations;
                }
            }
        }
    }
}
