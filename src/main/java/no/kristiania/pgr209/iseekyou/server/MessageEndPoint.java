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
    @Path("/user/color")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<User> findUser(@QueryParam("userColor") int id) throws SQLException {
        return List.of(userDao.retrieve(id));
    }

    //Should show a settings window when a user is selected in drop-down menu. Can then change user settings.
    @Path("/user/settings")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void userSettings(int userId) {
        /*
            POST - allow for editing of user settings.
                   Need to update run queries to update database
         */
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


    /*
    @Path
    // Method should be able to handle new message
     */

    /*
    @Path
    // Method should be able to reply to an existing thread
     */








}
