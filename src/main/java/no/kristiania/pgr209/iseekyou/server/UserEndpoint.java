package no.kristiania.pgr209.iseekyou.server;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import no.kristiania.pgr209.iseekyou.User;
import no.kristiania.pgr209.iseekyou.database.UserDao;

import java.sql.SQLException;
import java.util.List;

@Path("/")
public class UserEndpoint {

    @Inject
    public UserDao userDao;

    //Lists all users for drop-down menu in front-end
    @Path("/user")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<User> listAllUsers() throws SQLException {
        return userDao.listAll();
    }

    @Path("/user/new")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void saveNewUser(User user) throws SQLException {
        userDao.save(user);
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
}