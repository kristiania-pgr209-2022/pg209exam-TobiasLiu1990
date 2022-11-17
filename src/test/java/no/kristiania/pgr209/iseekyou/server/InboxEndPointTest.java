package no.kristiania.pgr209.iseekyou.server;

import jakarta.json.Json;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

public class InboxEndPointTest extends AbstractServerTest{

    @Test
    void shouldListUsersConversations() throws IOException {
        var connection = openConnection("/api/user/inbox?userId=" + 1);
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-Type" , "application/json");
        connection.setDoOutput(true);

        assertThat(connection.getResponseCode())
                .as(connection.getResponseMessage() + " for " + connection.getURL())
                .isEqualTo(200);

        assertThat(connection.getInputStream())
                .asString(StandardCharsets.UTF_8)
                .contains("\"conversationTitle\":\"Another conversation between Elon, Ghandi from Ola\",\"id\":2}");
    }

    @Test
    void shouldListParticipantsInAConversationExceptTheCurrentLoggedInUser() throws IOException {
        var connection = openConnection(
                "/api/user/inbox/conversation/members?userId=" + 3 + "&conversationId=" + 2);
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-Type" , "application/json");
        connection.setDoOutput(true);

        assertThat(connection.getResponseCode())
                .as(connection.getResponseMessage() + " for " + connection.getURL())
                .isEqualTo(200);

        assertThat(connection.getInputStream())
                .asString(StandardCharsets.UTF_8)
                .contains("[\"Elon Musk\",\"Mahamta Ghandi\",\"Ola Nordman\"]");
    }

    @Test
    void shouldListAllMessagesSpecificToTheClickedConversation() throws IOException {
        var connection = openConnection(
        "/api/user/inbox/conversation/messages?conversationId=" + 2);
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-Type" , "application/json");
        connection.setDoOutput(true);

        assertThat(connection.getResponseCode())
                .as(connection.getResponseMessage() + " for " + connection.getURL())
                .isEqualTo(200);

        assertThat(connection.getInputStream())
                .asString(StandardCharsets.UTF_8)
                .contains("\"senderName\":\"Mahamta Ghandi\"},{\"content\":\"Yeah Elon, go back to twitter!");
    }

    @Test
    void shouldSaveAReplyToTheConversationThatIsAddedAtTheEndIfTheConversation() throws IOException {
        var postConnection = openConnection("/api/user/inbox/conversation/message/reply");
        postConnection.setRequestMethod("POST");
        postConnection.setRequestProperty("Content-Type" , "application/json");
        postConnection.setDoOutput(true);
        postConnection.getOutputStream().write(
                Json.createObjectBuilder()
                        .add("senderId", 3)
                        .add("content", "Posting a little test message")
                        .add("conversationId", 2)
                        .build()
                        .toString()
                        .getBytes(StandardCharsets.UTF_8)
        );

        assertThat(postConnection.getResponseCode())
                .as(postConnection.getResponseMessage() + " for " + postConnection.getURL())
                .isEqualTo(200);

        var connection = openConnection("/api/user/inbox/conversation/messages?conversationId=" + 2);
        assertThat(connection.getInputStream())
                .asString(StandardCharsets.UTF_8)
                .contains(",{\"content\":\"Posting a little test message\",");
    }

}