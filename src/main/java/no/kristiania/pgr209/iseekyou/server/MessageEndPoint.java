package no.kristiania.pgr209.iseekyou.server;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import no.kristiania.pgr209.iseekyou.Conversation;
import no.kristiania.pgr209.iseekyou.Message;
import no.kristiania.pgr209.iseekyou.User;
import no.kristiania.pgr209.iseekyou.database.ConversationDao;
import no.kristiania.pgr209.iseekyou.database.MessageDao;
import no.kristiania.pgr209.iseekyou.database.UserDao;

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
    public List<User> findUser(@QueryParam("userColor") int id) throws SQLException {
        return List.of(userDao.retrieve(id));
    }

    //Should let user change username
    @Path("/user/settings/changename")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void userSettingsForName(@QueryParam("userId") int id, User user) throws SQLException {
        userDao.updateUserName(user, id);
    }

    //Should let user change email
    @Path("/user/settings/changeemail")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void userSettingsForEmail(@QueryParam("userId") int id, User user) throws SQLException {
        userDao.updateEmail(user, id);
    }

    //Should let user change favorite color
    @Path("/user/settings/changecolor")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void userSettingsForFavoriteColor(@QueryParam("userId") int id, User user) throws SQLException {
        userDao.updateFavoriteColor(user, id);
    }


    //Shows all conversations when a user is selected in drop-down menu.
    @Path("/user/inbox")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Conversation> userConversations(@QueryParam("userId") int userId) throws SQLException {
        return conversationDao.retrieveAllConversationsByUserId(userId);
    }

    //Shows all messages in a conversation when a conversation is clicked.
    @Path("user/inbox/messages")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Message> conversationMessages(@QueryParam("conversationId") int conversationId) throws SQLException {
        return messageDao.retrieveAllMessagesByConversationId(conversationId);
    }



    //Create new conversation
    @Path("user/inbox/new")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void newConversation(Conversation conversation) throws SQLException {
        conversationDao.save(conversation);
    }

    //Find the newest id for conversation created
    @Path("user/inbox/new/conversationId")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    public Conversation findLatestConversation() throws SQLException {
        return conversationDao.retrieveLastConversation();
    }

    //Find all users except current
    @Path("user/inbox/new/conversationMembers")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    public List<User> newConversationUsers(@QueryParam("userId") int userId) throws SQLException {
        return userDao.getAllUsersExceptSender(userId);
    }








    /*
    @Path
    // Method should be able to handle new message
     */

    /*
    @Path
    // Method should be able to reply to an existing thread
     */








}
