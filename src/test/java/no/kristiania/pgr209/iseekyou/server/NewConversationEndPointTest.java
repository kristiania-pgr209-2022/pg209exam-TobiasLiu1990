package no.kristiania.pgr209.iseekyou.server;

import jakarta.json.Json;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

public class NewConversationEndPointTest extends AbstractServerTest{

//    @Test
//    void shouldCreateANewConversation() throws IOException {
//        var postConnection = openConnection("/api/user/inbox/new/conversation");
//        postConnection.setRequestMethod("POST");
//        postConnection.setRequestProperty("Content-Type" , "application/json");
//        postConnection.setDoOutput(true);
//        postConnection.getOutputStream().write(
//                Json.createObjectBuilder()
//                        .add("conversationTitle", "This is a new test-conversation")
//                        .build()
//                        .toString()
//                        .getBytes(StandardCharsets.UTF_8)
//        );
//
//        assertThat(postConnection.getResponseCode())
//                .as(postConnection.getResponseMessage() + " for " + postConnection.getURL())
//                .isEqualTo(200);
//
//        var connection = openConnection("/api/user/inbox");
//        assertThat(connection.getInputStream())
//                .asString(StandardCharsets.UTF_8)
//                .contains(",{\"content\":\"Posting a little test message\",");
//    }

    @Test
    void shouldGetAllUsersExceptTheCurrentLoggedInUser() throws IOException {
        var postConnection = openConnection(
                "/api/user/inbox/new/conversationRecipients?userId=" + 1);
        postConnection.setRequestMethod("GET");
        postConnection.setRequestProperty("Content-Type" , "application/json");
        postConnection.setDoOutput(true);

        assertThat(postConnection.getResponseCode())
                .as(postConnection.getResponseMessage() + " for " + postConnection.getURL())
                .isEqualTo(200);

        var connection = openConnection("/api/user/inbox/new/conversationRecipients?userId=" + 1);
        assertThat(connection.getInputStream())
                .asString(StandardCharsets.UTF_8)
                .contains("[{\"age\":64,\"color\":\"yellow\",\"email\":\"snorre@gmail.com\",\"fullName\":\"Snorre Snorreson");
    }

    @Test
    void shouldAddRecipientsToAConversation() throws IOException {
        var postConnection = openConnection("/api/user/inbox/new/conversation/addRecipients");
        postConnection.setRequestMethod("POST");
        postConnection.setRequestProperty("Content-Type" , "application/json");
        postConnection.setDoOutput(true);
        postConnection.getOutputStream().write(
                Json.createObjectBuilder()
                        .add("recipientId", 4)
                        .add("conversationId", 1)
                        .build()
                        .toString()
                        .getBytes(StandardCharsets.UTF_8)
        );

        assertThat(postConnection.getResponseCode())
                .as(postConnection.getResponseMessage() + " for " + postConnection.getURL())
                .isEqualTo(200);

        var connection = openConnection("/api/user/inbox?userId=" + 4);
        assertThat(connection.getInputStream())
                .asString(StandardCharsets.UTF_8)
                .contains("{\"conversationTitle\":\"A little conversation from Ola to Snorre\",\"id\":1}");
    }

    @Test
    void shouldAddAReplyToAConversation() throws IOException {
        var postConnection = openConnection("/api/user/inbox/conversation/message/reply");
        postConnection.setRequestMethod("POST");
        postConnection.setRequestProperty("Content-Type" , "application/json");
        postConnection.setDoOutput(true);
        postConnection.getOutputStream().write(
                Json.createObjectBuilder()
                        .add("senderId", 4)
                        .add("content", "Testing reply to a conversation")
                        .add("conversationId", 1)
                        .build()
                        .toString()
                        .getBytes(StandardCharsets.UTF_8)
        );

        assertThat(postConnection.getResponseCode())
                .as(postConnection.getResponseMessage() + " for " + postConnection.getURL())
                .isEqualTo(200);

        var connection = openConnection("/api/user/inbox/conversation/messages?conversationId=" + 1);
        assertThat(connection.getInputStream())
                .asString(StandardCharsets.UTF_8)
                .contains("{\"content\":\"Testing reply to a conversation");
    }
}