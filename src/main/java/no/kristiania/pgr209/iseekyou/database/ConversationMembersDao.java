package no.kristiania.pgr209.iseekyou.database;

import jakarta.inject.Inject;
import no.kristiania.pgr209.iseekyou.ConversationMembers;

import javax.sql.DataSource;
import java.sql.SQLException;

public class ConversationMembersDao {

    private final DataSource dataSource;

    @Inject
    public ConversationMembersDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    //Save
    public void save(ConversationMembers recipients) throws SQLException {
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
