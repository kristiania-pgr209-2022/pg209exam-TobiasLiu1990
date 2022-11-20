package no.kristiania.pgr209.iseekyou.server;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import no.kristiania.pgr209.iseekyou.Conversation;
import no.kristiania.pgr209.iseekyou.Message;
import no.kristiania.pgr209.iseekyou.database.*;

import java.sql.SQLException;
import java.util.List;

@Path("/")
public class InboxEndPoint {

    @Inject
    public ConversationDao conversationDao;

    @Inject
    public MessageDao messageDao;

    @Inject
    public ConversationMembersDao conversationMembersDao;

    //Shows all conversations for user
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Conversation> retrieveAllConversationsForUser(@QueryParam("userId") int userId) throws SQLException {
        return conversationDao.retrieveAllConversationsByUserId(userId);
    }

    //Get everyone in each conversation
    @Path("/conversation/members")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<String> retrieveConversationParticipants(@QueryParam("userId") int userId, @QueryParam("conversationId") int conversationId) throws SQLException {
        return conversationMembersDao.retrieveConversationParticipantsExceptCurrentUser(userId, conversationId);
    }

    //Shows all messages in a conversation when a conversation is clicked.
    @Path("/conversation/messages")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Message> retrieveConversationMessages(@QueryParam("conversationId") int conversationId) throws SQLException {
        return messageDao.retrieveAllMessagesByConversationId(conversationId);
    }

    //Reply to a conversation.
    @Path("/conversation/message/reply")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Message replyToConversation(Message message) throws SQLException {
        return messageDao.save(message);
    }
}