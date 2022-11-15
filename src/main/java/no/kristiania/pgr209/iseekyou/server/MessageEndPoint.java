package no.kristiania.pgr209.iseekyou.server;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import no.kristiania.pgr209.iseekyou.Conversation;
import no.kristiania.pgr209.iseekyou.ConversationMembers;
import no.kristiania.pgr209.iseekyou.Message;
import no.kristiania.pgr209.iseekyou.User;
import no.kristiania.pgr209.iseekyou.database.*;

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

    //@Inject
    //public ConversationMembersDao conversationMembersDao;

    //Lists all users for drop-down menu in front-end
    @Path("/user")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<User> listAllUsers() throws SQLException {
        return userDao.listAll();
    }

    //Runs after above method. This is to get the user again to change font color based on user selected.
    @Path("/user/setcolor")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public User findUser(@QueryParam("userColor") int id) throws SQLException {
        return userDao.retrieve(id);
    }

    //Updates user settings if changed fields are not empty.
    @Path("/user/settings")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void updateUserSettings(@QueryParam("userId") int id, User user) throws SQLException {
        //Maybe fet user to compare if changes are made first as well?

        if (!user.getFullName().equals("")) {
            userDao.updateUserName(user, id);
        }
        if (!user.getEmail().equals("")) {
            userDao.updateEmail(user, id);
        }
        if (!user.getColor().equals("")) {
            userDao.updateFavoriteColor(user, id);
        }
    }

    //Should let user change username
//    @Path("/user/settings/changename")
//    @POST
//    @Consumes(MediaType.APPLICATION_JSON)
//    public void userSettingsForName(@QueryParam("userId") int id, User user) throws SQLException {
//        userDao.updateUserName(user, id);
//    }
//
//    //Should let user change email
//    @Path("/user/settings/changeemail")
//    @POST
//    @Consumes(MediaType.APPLICATION_JSON)
//    public void userSettingsForEmail(@QueryParam("userId") int id, User user) throws SQLException {
//        userDao.updateEmail(user, id);
//    }
//
//    //Should let user change favorite color
//    @Path("/user/settings/changecolor")
//    @POST
//    @Consumes(MediaType.APPLICATION_JSON)
//    public void userSettingsForFavoriteColor(@QueryParam("userId") int id, User user) throws SQLException {
//        userDao.updateFavoriteColor(user, id);
//    }

    //Shows all conversations when a user is selected in drop-down menu.
    @Path("/user/inbox")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Conversation> userConversations(@QueryParam("userId") int userId) throws SQLException {
        return conversationDao.retrieveAllConversationsByUserId(userId);
    }

    //Shows all messages in a conversation when a conversation is clicked.
    @Path("/user/inbox/messages")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Message> conversationMessages(@QueryParam("conversationId") int conversationId) throws SQLException {
        return messageDao.retrieveAllMessagesByConversationId(conversationId);
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
    public void addConversationUsers(ConversationMembers recipient) throws SQLException {
        System.out.println("Conversation ID: " + recipient.getConversationId());
        System.out.println("Recipients ID: " + recipient.getRecipientId());

        //Query to add to Conversation_Members table
    }

    @Path("/user/inbox/new/conversation/message")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public void addConversationMessage(Message message) {
        //Add message to DB
    }



}
