package no.kristiania.pgr209.iseekyou.database;

import no.kristiania.pgr209.iseekyou.Conversation;
import no.kristiania.pgr209.iseekyou.ConversationMembers;
import no.kristiania.pgr209.iseekyou.InMemoryDataSource;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;

import static no.kristiania.pgr209.iseekyou.GenerateSampleData.sampleUser;
import static org.assertj.core.api.Assertions.assertThat;

public class ConversationDatabaseTest {

    private final JdbcDataSource dataSource = (JdbcDataSource) InMemoryDataSource.createDataSource();
    private final ConversationDao conversationDao = new ConversationDao(dataSource);
    private final ConversationMembersDao conversationMembersDao = new ConversationMembersDao(dataSource);

    private final UserDao userDao = new UserDao(dataSource);

    @Test
    void shouldSaveConversation() throws SQLException {


    }


    @Test
    void shouldSaveConversationAndRetrieveAllByUserId() throws SQLException {
        var user = sampleUser();
        userDao.save(user);

        var conversation = new Conversation("Backendprogramming");
        conversationDao.save(conversation);

        var conversationMembers = new ConversationMembers();
        conversationMembers.setRecipientId(user.getId());
        conversationMembers.setConversationId(conversation.getId());

        conversationMembersDao.save(conversationMembers);


        List<Conversation> conversationList = conversationDao.retrieveAllConversationsByUserId(user.getId());
        Conversation savedConversation = new Conversation();

        for (Conversation c : conversationList) {
            if (c.getId() == conversation.getId()) {
                savedConversation = c;
            }
        }

        assertThat(conversation)
                .hasNoNullFieldsOrProperties()
                .usingRecursiveComparison()
                .isEqualTo(savedConversation)
                .isNotSameAs(conversationList);
    }



}
