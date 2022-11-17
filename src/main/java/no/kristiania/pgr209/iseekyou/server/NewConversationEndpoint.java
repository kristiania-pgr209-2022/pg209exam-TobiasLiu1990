package no.kristiania.pgr209.iseekyou.server;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import no.kristiania.pgr209.iseekyou.Conversation;
import no.kristiania.pgr209.iseekyou.ConversationMembers;
import no.kristiania.pgr209.iseekyou.Message;
import no.kristiania.pgr209.iseekyou.User;
import no.kristiania.pgr209.iseekyou.database.ConversationDao;
import no.kristiania.pgr209.iseekyou.database.ConversationMembersDao;
import no.kristiania.pgr209.iseekyou.database.MessageDao;
import no.kristiania.pgr209.iseekyou.database.UserDao;

import java.sql.SQLException;
import java.util.List;

@Path("/")
public class NewConversationEndpoint {

    @Inject
    public UserDao userDao;

    @Inject
    public ConversationDao conversationDao;

    @Inject
    public MessageDao messageDao;

    @Inject
    public ConversationMembersDao conversationMembersDao;

    //Create new conversation
    //Will return the id of created conversation.
    //To avoid if many uses/posts new conversations, ID could otherwise be wrong if fetched in different method
    @Path("/conversation")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public int newConversation(Conversation conversation) throws SQLException {
        return conversationDao.save(conversation);
    }

    //Find all recipients except current
    @Path("/conversationRecipients")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    public List<User> getConversationUsers(@QueryParam("userId") int userId) throws SQLException {
        return userDao.getAllUsersExceptSender(userId);
    }

    //ADD recipients for new conversation
    @Path("/conversation/addRecipients")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public int addConversationUsers(ConversationMembers recipient) throws SQLException {
        System.out.println("user id: " + recipient.getRecipientId());
        System.out.println("conversation id: " + recipient.getConversationId());

        return conversationMembersDao.save(recipient);
    }

    @Path("/conversation/message")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public void addConversationMessage(Message message) throws SQLException {
        messageDao.save(message);
    }
}