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
public class InboxEndPoint {

    @Inject
    public ConversationDao conversationDao;

    @Inject
    public MessageDao messageDao;

    @Inject
    public ConversationMembersDao conversationMembersDao;

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
    public List<String> conversationParticipants(@QueryParam("userId") int userId, @QueryParam("conversationId") int conversationId) throws SQLException {
        return conversationMembersDao.getConversationParticipants(userId, conversationId);
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


}