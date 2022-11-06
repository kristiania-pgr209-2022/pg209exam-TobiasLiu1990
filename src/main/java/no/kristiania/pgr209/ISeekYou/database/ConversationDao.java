package no.kristiania.pgr209.ISeekYou.database;

import no.kristiania.pgr209.ISeekYou.Conversation;

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
                stmt.setString(1, conversation.getConversationName());
                stmt.executeUpdate();
                try (var generatedKeys = stmt.getGeneratedKeys()) {
                    generatedKeys.next();
                    conversation.setId(generatedKeys.getInt(1));
                }
            }
        }
    }

    public List<Conversation> retrieveAllThreads(int id) throws SQLException {
        try (var connection = dataSource.getConnection()) {
            String query = """
                    select conversation_name from Conversation
                    JOIN Group_members ON Conversation.conversation_id = Group_members.conversation_id
                    JOIN User ON Group_members.user_id = User.user_id
                    where user_id = ?
                    """;
            try (var stmt = connection.prepareStatement(query)) {
                stmt.setInt(1, id);
                try (var resultSet = stmt.executeQuery()) {
                    List<Conversation> allConversations = new ArrayList<>();
                    while (resultSet.next()) {
                        Conversation conversation = new Conversation();
                        conversation.setId(resultSet.getInt(1));
                        conversation.setConversationName(resultSet.getString(2));
                    }
                    return allConversations;
                }
            }
        }
    }

}
