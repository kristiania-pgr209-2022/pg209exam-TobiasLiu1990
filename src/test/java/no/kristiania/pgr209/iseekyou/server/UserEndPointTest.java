package no.kristiania.pgr209.iseekyou.server;

import jakarta.json.Json;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

public class UserEndPointTest extends AbstractServerTest {

    @Test
    void shouldListUsers() throws IOException {
        var connection = openConnection("/api/user");

        assertThat(connection.getResponseCode())
                .as(connection.getResponseMessage() + " for " + connection.getURL())
                .isEqualTo(200);

        assertThat(connection.getInputStream())
                .asString(StandardCharsets.UTF_8)
                .contains("age\":73,\"color\":\"brown\",\"email\":\"peace@nowar.in\",\"fullName\":\"Mahamta Ghandi\",\"id\":5");
    }

    @Test
    void shouldSaveNewUser() throws IOException {
        var postConnection = openConnection("/api/user/new");
        postConnection.setRequestMethod("POST");
        postConnection.setRequestProperty("Content-Type", "application/json");
        postConnection.setDoOutput(true);
        postConnection.getOutputStream().write(
                Json.createObjectBuilder()
                        .add("fullName", "Im a testcrashdummy")
                        .add("email", "Test@Junit.gg.com")
                        .add("age", 25)
                        .add("color", "blue")
                        .build()
                        .toString()
                        .getBytes(StandardCharsets.UTF_8)
        );

        assertThat(postConnection.getResponseCode())
                .as(postConnection.getResponseMessage() + " for " + postConnection.getURL())
                .isEqualTo(201);

        var connection = openConnection("/api/user");
        assertThat(connection.getInputStream())
                .asString(StandardCharsets.UTF_8)
                .contains("color\":\"blue\",\"email\":\"Test@Junit.gg.com\",\"fullName\":\"Im a testcrashdummy\",\"id\":");
    }

    @Test
    void shouldUpdateAllUsersSettings() throws IOException {
        var putConnection = openConnection("/api/user/settings");
        putConnection.setRequestMethod("PUT");
        putConnection.setRequestProperty("Content-Type", "application/json");
        putConnection.setDoOutput(true);
        putConnection.getOutputStream().write(
                Json.createObjectBuilder()
                        .add("id", 6)
                        .add("fullName", "My new test name")
                        .add("email", "mynewtestemail@Junit.gg")
                        .add("age", 100)
                        .add("color", "yellow")
                        .build()
                        .toString()
                        .getBytes(StandardCharsets.UTF_8)
        );

        assertThat(putConnection.getResponseCode())
                .as(putConnection.getResponseMessage() + " for " + putConnection.getURL())
                .isEqualTo(204);

        var connection = openConnection("/api/user");
        assertThat(connection.getInputStream())
                .asString(StandardCharsets.UTF_8)
                .contains("{\"age\":100,\"color\":\"yellow\",\"email\":\"mynewtestemail@Junit.gg\",\"fullName\":\"My new test name\",\"id\":6");
    }

    @Test
    void shouldFailUpdatingUser() throws IOException {
        var putConnection = openConnection("/api/user/settings");
        putConnection.setRequestMethod("PUT");
        putConnection.setRequestProperty("Content-Type", "application/json");
        putConnection.setDoOutput(true);
        putConnection.getOutputStream().write(
                Json.createObjectBuilder()
                        .add("fullName", "Only a fullname")
                        .add("email", "")
                        .build()
                        .toString()
                        .getBytes(StandardCharsets.UTF_8)
        );

        assertThat(putConnection.getResponseCode())
                .as(putConnection.getResponseMessage() + " for " + putConnection.getURL())
                .isEqualTo(400);
    }
}