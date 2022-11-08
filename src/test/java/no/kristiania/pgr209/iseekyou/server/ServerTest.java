package no.kristiania.pgr209.iseekyou.server;

import no.kristiania.pgr209.iseekyou.InMemoryDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

public class ServerTest {

    private MessageServer server;
    private final DataSource dataSource = InMemoryDataSource.createDataSource();

    @BeforeEach
    void setUp() throws Exception {
        server = new MessageServer(0, dataSource);
        server.start();
    }

    @Test
    void shouldServerHomePage() throws IOException {
        var connection = openConnection("/");

        assertThat(connection.getResponseCode())
                .as(connection.getResponseMessage() + " for " + connection.getURL())
                .isEqualTo(200);

        assertThat(connection.getInputStream())
                .asString(StandardCharsets.UTF_8)
                .contains("<title>I Seek You</title>");
    }


    private HttpURLConnection openConnection(String path) throws IOException {
        return (HttpURLConnection) new URL(server.getURL(), path).openConnection();
    }
}