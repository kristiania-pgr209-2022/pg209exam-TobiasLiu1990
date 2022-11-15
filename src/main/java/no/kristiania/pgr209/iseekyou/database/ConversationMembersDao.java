package no.kristiania.pgr209.iseekyou.database;

import jakarta.inject.Inject;
import no.kristiania.pgr209.iseekyou.ConversationMembers;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;

public class ConversationMembersDao extends AbstractDao<ConversationMembers> {

    public ConversationMembersDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public List<ConversationMembers> listAll() throws SQLException {
        return null;
    }

    public int save(ConversationMembers recipients) throws SQLException {
        try (var connection = dataSource.getConnection()) {
            String query = "";
            try (var stmt = connection.prepareStatement(query)) {
                stmt.setInt(1, recipients.getRecipientId());
                stmt.setInt(2, recipients.getConversationId());
                stmt.executeUpdate();
            }
        }
    }
}