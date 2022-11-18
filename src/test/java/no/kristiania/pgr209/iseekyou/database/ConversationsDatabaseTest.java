package no.kristiania.pgr209.iseekyou.database;

import no.kristiania.pgr209.iseekyou.Conversation;
import no.kristiania.pgr209.iseekyou.ConversationMembers;
import no.kristiania.pgr209.iseekyou.InMemoryDataSource;
import no.kristiania.pgr209.iseekyou.User;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static no.kristiania.pgr209.iseekyou.GenerateSampleData.sampleUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConversationsDatabaseTest {

    private final JdbcDataSource dataSource = (JdbcDataSource) InMemoryDataSource.createDataSource();
    private final ConversationDao conversationDao = new ConversationDao(dataSource);
    private final ConversationMembersDao conversationMembersDao = new ConversationMembersDao(dataSource);
    private final UserDao userDao = new UserDao(dataSource);

    private final List<User> users = new ArrayList<>();
    private final List<ConversationMembers> participants = new ArrayList<>();
    private final int totalParticipants = 10;

    @BeforeEach
    void init() throws SQLException {
        for (int i = 0; i < totalParticipants; i++) {
            var user = sampleUser();
            users.add(user);
            userDao.save(user);
            participants.add(new ConversationMembers());
        }
    }

    @Test
    void shouldSaveConversationAndRetrieveAllByUserId() throws SQLException {
        var backendConversation = new Conversation("Backendprogramming");
        var softwareConversation = new Conversation("Software Design");
        var apiConversation = new Conversation("API");
        conversationDao.save(backendConversation);
        conversationDao.save(softwareConversation);
        conversationDao.save(apiConversation);

        //Save all users to be in "Backendprogramming" conversation.
        for (int i = 0; i < totalParticipants; i++) {
            participants.get(i).setRecipientId(users.get(i).getId());
            participants.get(i).setConversationId(backendConversation.getId());
            conversationMembersDao.save(participants.get(i));
        }

        //Now adding user 0 to software design and api.
        var softwareParticipant = new ConversationMembers();
        var apiParticipant = new ConversationMembers();

        softwareParticipant.setRecipientId(users.get(0).getId());
        softwareParticipant.setConversationId(softwareConversation.getId());
        conversationMembersDao.save(softwareParticipant);

        apiParticipant.setRecipientId(users.get(0).getId());
        apiParticipant.setConversationId(apiConversation.getId());
        conversationMembersDao.save(apiParticipant);

        List<Conversation> conversationList = conversationDao.retrieveAllConversationsByUserId(users.get(0).getId());

        Conversation savedConversation = new Conversation();
        for (Conversation c : conversationList) {
            if (c.getId() == backendConversation.getId()) {
                savedConversation = c;
            }
        }

        assertEquals(conversationList.size(), 3);

        assertThat(conversationList)
                .extracting(Conversation::getConversationTitle)
                .contains(backendConversation.getConversationTitle())
                .contains(softwareConversation.getConversationTitle())
                .contains(apiConversation.getConversationTitle());

        assertThat(backendConversation)
                .hasNoNullFieldsOrProperties()
                .usingRecursiveComparison()
                .isEqualTo(savedConversation)
                .isNotSameAs(conversationList);
    }

    @Test
    void shouldRetrieveAllConversationParticipantsExceptCurrentUser() throws SQLException {
        var user1 = sampleUser();
        userDao.save(user1);
        var user2 = sampleUser();
        userDao.save(user2);

        var conversation = new Conversation("Christmas");
        conversationDao.save(conversation);

        var conversationMember1 = new ConversationMembers();
        var conversationMember2 = new ConversationMembers();
        conversationMember1.setRecipientId(user1.getId());
        conversationMember1.setConversationId(conversation.getId());
        conversationMember2.setRecipientId(user2.getId());
        conversationMember2.setConversationId(conversation.getId());

        conversationMembersDao.save(conversationMember1);
        conversationMembersDao.save(conversationMember2);

        List<String> conversationParticipants = conversationMembersDao.retrieveConversationParticipantsExceptCurrentUser(user1.getId(), conversation.getId());
        assertEquals(1, conversationParticipants.size());
    }
}
