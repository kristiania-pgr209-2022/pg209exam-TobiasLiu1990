package no.kristiania.pgr209.ISeekYou.server;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import no.kristiania.pgr209.ISeekYou.Group;
import no.kristiania.pgr209.ISeekYou.User;

import java.util.List;

@Path("/")
public class MessageEndPoint {


    @Path("/user")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<User> getInboxThreads() {
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


    @Path("/user/inbox")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Group> userMessages() {
        /*
            Get groups by user_id -> returns all messages.
         */
        return groupDao.getGroupById();
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
