package no.kristiania.pgr209.iseekyou.database;

import no.kristiania.pgr209.iseekyou.Conversation;
import no.kristiania.pgr209.iseekyou.InMemoryDataSource;
import no.kristiania.pgr209.iseekyou.Message;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.registerCustomDateFormat;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MessageDatabaseTest {

    private final JdbcDataSource dataSource = (JdbcDataSource) InMemoryDataSource.createDataSource();
    private final MessageDao messageDao = new MessageDao(dataSource);
    private final UserDao userDao = new UserDao(dataSource);
    private final ConversationDao conversationDao = new ConversationDao(dataSource);

    @Test
    void shouldSaveMessageToDatabase() throws SQLException {
        var testMessage = new Message();
        testMessage.setContent("This is a test message");
        testMessage.setConversationId(3);
        testMessage.setSenderId(5);
        messageDao.save(testMessage);

        var getAllMessagesById = messageDao.retrieveAllMessagesByConversationId(3);
        getAllMessagesById.stream().map(Message::getContent).forEach(System.out::println);

        var lastMessageInConversation = getAllMessagesById.get(getAllMessagesById.size()-1);

        assertThat(lastMessageInConversation.getContent())
                .isEqualTo(testMessage.getContent());
    }

    @Test
    void retrieveAllShouldReturnRightAmountOfMessages() throws SQLException {
        var testConversation = new Conversation();
        testConversation.setConversationTitle("Test testConversation");
        conversationDao.save(testConversation);

        List<Message> fifteenMessages = new ArrayList<>();

        for (int i = 0; i <= 15; i++){
            var message = new Message();
            message.setContent("I can count to " + i);
            message.setSenderId(5);
            message.setConversationId(testConversation.getId());
            fifteenMessages.add(message);
        }

        for (var fifteenMessage : fifteenMessages){
            messageDao.save(fifteenMessage);
        }

        var returnedMessages = messageDao.retrieveAllMessagesByConversationId(testConversation.getId());

        assertThat(returnedMessages.size()).isEqualTo(fifteenMessages.size());
    }

    @Test
    void retrievedMessageShouldShowSendersName() throws SQLException {
        var content = "This is another test message";
        var testMessage = new Message(5, content);
        testMessage.setConversationId(2);
        messageDao.save(testMessage);

        var getAllMessagesById = messageDao.retrieveAllMessagesByConversationId(2);

        var lastMessageInConversation = getAllMessagesById.get(getAllMessagesById.size()-1);
        var userThatWroteTheMessage = userDao.retrieve(testMessage.getSenderId());

        assertThat(lastMessageInConversation.getSenderName())
                .isEqualTo(userThatWroteTheMessage.getFullName());
    }

    @Test
    void retrievedMessagesShouldBeInChronologicalOrder() throws SQLException {
        var content = "This is another test message";
        var testMessage = new Message(5, content);
        testMessage.setConversationId(2);
        messageDao.save(testMessage);

        var getAllMessagesById = messageDao.retrieveAllMessagesByConversationId(2);

        var lastMessageInConversation = getAllMessagesById.get(getAllMessagesById.size()-1);
        var almostLastMessageInConversation = getAllMessagesById.get(getAllMessagesById.size()-2);
        var timestamp = lastMessageInConversation.getMessageDate();
        var timestamp2 = almostLastMessageInConversation.getMessageDate();

        assertTrue(timestamp2.isBefore(timestamp));
    }
}