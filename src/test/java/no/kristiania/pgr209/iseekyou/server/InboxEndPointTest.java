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
                .contains("\"conversationTitle\":\"Another coversation between Elon, Ghandi from Ola\",\"id\":2}");
    }

    @Test
    void shouldListParticipantsInAnConversationExceptTheCurrentLoggedInUser() throws IOException {
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

}