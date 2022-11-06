package no.kristiania.pgr209.ISeekYou.database;

import no.kristiania.pgr209.ISeekYou.Message;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.sql.Statement;

public class MessageDao {

    private DataSource dataSource;

    public MessageDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void save(Message message, String fullName, int conversationId) throws SQLException {
        try (var connection = dataSource.getConnection()) {
            String query = "insert into message (sender, date, content, conversation_id) values (?, ?, ?, ?) ";
            try (var stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, fullName);
                stmt.setDate(2, message.getMessageDate());
                stmt.setString(3, message.getMessageText());
                stmt.setInt(4, conversationId);
            }
        }
    }

}
