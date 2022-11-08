package no.kristiania.pgr209.ISeekYou.server;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import no.kristiania.pgr209.ISeekYou.Conversation;
import no.kristiania.pgr209.ISeekYou.Group;
import no.kristiania.pgr209.ISeekYou.User;
import no.kristiania.pgr209.ISeekYou.database.ConversationDao;
import no.kristiania.pgr209.ISeekYou.database.UserDao;

import java.sql.SQLException;
import java.util.List;

@Path("/")
public class MessageEndPoint {

    public UserDao userDao;
    public ConversationDao conversationDao;

    @Path("/user")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<User> getAllUsers() throws SQLException {
        return userDao.listAll();
    }

    @Path("/user")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void userSettings() {
        /*
            POST - allow for editing of user settings.
                   Need to update run queries to update database
         */
    }

    @Path("/{user_id}/inbox")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Conversation> userMessages(@PathParam("user_id") int id) throws SQLException {
        /*
            Get groups by user_id -> returns all messages.
         */
        return conversationDao.retrieveAllConversationsByUserId(id);
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
