package no.kristiania.pgr209.iseekyou.server;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import no.kristiania.pgr209.iseekyou.Conversation;
import no.kristiania.pgr209.iseekyou.ConversationMembers;
import no.kristiania.pgr209.iseekyou.Message;
import no.kristiania.pgr209.iseekyou.User;
import no.kristiania.pgr209.iseekyou.database.*;

import javax.print.attribute.standard.Media;
import java.sql.SQLException;
import java.util.List;

@Path("/")
public class MessageEndPoint {

    @Inject
    public UserDao userDao;

    @Inject
    public ConversationDao conversationDao;

    @Inject
    public MessageDao messageDao;

    @Inject
    public ConversationMembersDao conversationMembersDao;

    //Lists all users for drop-down menu in front-end
    @Path("/user")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<User> listAllUsers() throws SQLException {
        return userDao.listAll();
    }

    //Updates user settings if changed fields are not empty.
    @Path("/user/settings")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void updateUserSettings(User user) throws SQLException {
        if (!user.getFullName().equals("")) {
            userDao.updateUserName(user);
        }
        if (!user.getEmail().equals("")) {
            userDao.updateEmail(user);
        }
        if (!user.getColor().equals("")) {
            userDao.updateFavoriteColor(user);
        }
        if (user.getAge() > 0) {
            userDao.updateAge(user);
        }
    }

    //Shows all conversations for user
    @Path("/user/inbox")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Conversation> userConversations(@QueryParam("userId") int userId) throws SQLException {
        return conversationMembersDao.retrieveAllConversationsByUserId(userId);
    }

    //Get everyone in each conversation
    @Path("/user/inbox/conversation/members")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<String> conversationParticipants(@QueryParam("userId") int userId, @QueryParam("cId") int conversationId) throws SQLException {
        return userDao.getConversationParticipants(userId, conversationId);
    }

    //Shows all messages in a conversation when a conversation is clicked.
    @Path("/user/inbox/conversation/messages")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Message> conversationMessages(@QueryParam("conversationId") int conversationId) throws SQLException {
        return messageDao.retrieveAllMessagesByConversationId(conversationId);
    }

    @Path("user/inbox/conversation/message/reply")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public String replyToConversation(Message message) throws SQLException {
        System.out.println("message id: " + message.getSenderId());
        System.out.println("content: " + message.getContent());
        System.out.println("conversation id: " + message.getConversationId());

        return messageDao.reply(message);
    }

    //Create new conversation
    //Will return the id of created conversation.
    //To avoid if many uses/posts new conversations, ID could otherwise be wrong if fetched in different method
    @Path("/user/inbox/new/conversation")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public int newConversation(Conversation conversation) throws SQLException {
        return conversationDao.save(conversation);
    }

    //Find all recipients except current
    @Path("/user/inbox/new/conversationRecipients")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    public List<User> getConversationUsers(@QueryParam("userId") int userId) throws SQLException {
        return userDao.getAllUsersExceptSender(userId);
    }

    //ADD recipients for new conversation
    @Path("/user/inbox/new/conversation/addRecipients")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public int addConversationUsers(ConversationMembers recipient) throws SQLException {
        return conversationMembersDao.save(recipient);
    }

    @Path("/user/inbox/new/conversation/message")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public void addConversationMessage(Message message) throws SQLException {
        messageDao.save(message);
    }
}