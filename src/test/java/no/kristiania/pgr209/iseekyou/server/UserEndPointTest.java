package no.kristiania.pgr209.iseekyou.server;

import jakarta.json.Json;
import no.kristiania.pgr209.iseekyou.InMemoryDataSource;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
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
                .contains(",\"fullName\":\"Ola Nordman\",\"id\":1},{\"age\":64,\"color\":\"black\",\"email\":\"snorre");
    }

    @Test
    void shouldSaveNewUser() throws IOException {
        var postConnection = openConnection("/api/user/new");
        postConnection.setRequestMethod("POST");
        postConnection.setRequestProperty("Content-Type" , "application/json");
        postConnection.setDoOutput(true);
        postConnection.getOutputStream().write(
                Json.createObjectBuilder()
                        .add("fullName", "I'm a testcrashdummy")
                        .add("email", "Test@Junit.gg")
                        .add("age", 25)
                        .add("color", "blue")
                        .build()
                        .toString()
                        .getBytes(StandardCharsets.UTF_8)
        );

        assertThat(postConnection.getResponseCode())
                .as(postConnection.getResponseMessage() + " for " + postConnection.getURL())
                .isEqualTo(204);

        var connection = openConnection("/api/user");
        assertThat(connection.getInputStream())
                .asString(StandardCharsets.UTF_8)
                .contains("color\":\"blue\",\"email\":\"Test@Junit.gg\",\"fullName\":\"I'm a testcrashdummy\",\"id\":6");
    }

    @Test
    void shouldUpdateAllUsersSettings() throws IOException {

        var putConnection = openConnection("/api/user/settings");
        putConnection.setRequestMethod("PUT");
        putConnection.setRequestProperty("Content-Type" , "application/json");
        putConnection.setDoOutput(true);
        putConnection.getOutputStream().write(
                Json.createObjectBuilder()
                        .add("id", 5)
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
                .contains("{\"age\":100,\"color\":\"yellow\",\"email\":\"mynewtestemail@Junit.gg\",\"fullName\":\"My new test name\",\"id\":5");
    }
}