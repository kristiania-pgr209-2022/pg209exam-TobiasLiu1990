package no.kristiania.pgr209.iseekyou.server;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import no.kristiania.pgr209.iseekyou.Conversation;
import no.kristiania.pgr209.iseekyou.User;
import no.kristiania.pgr209.iseekyou.database.ConversationDao;
import no.kristiania.pgr209.iseekyou.database.UserDao;

import java.sql.SQLException;
import java.util.List;

@Path("/")
public class MessageEndPoint {

    @Inject
    public UserDao userDao;

    @Inject
    public ConversationDao conversationDao;

    @Path("/user")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<User> listAllUsers() throws SQLException {
        return userDao.listAll();
    }


    @Path("/user/settings")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void userSettings(int userId) {
        /*
            POST - allow for editing of user settings.
                   Need to update run queries to update database
         */
    }


    @Path("/user/inbox")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Conversation> userConversations(@QueryParam("userId") int userId) throws SQLException {
        return conversationDao.retrieveAllConversationsByUserId(userId);
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
