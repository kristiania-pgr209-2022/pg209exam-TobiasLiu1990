package no.kristiania.pgr209.iseekyou.server;

import no.kristiania.pgr209.iseekyou.InMemoryDataSource;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class AbstractServerTest {
    private final JdbcDataSource dataSource = (JdbcDataSource) InMemoryDataSource.createDataSource();
    private ISeekYouMessagingServer server;

    @BeforeEach
    void setUp() throws Exception {
        server = new ISeekYouMessagingServer(0, dataSource);
        server.start();
    }

    protected HttpURLConnection openConnection(String path) throws IOException {
        return (HttpURLConnection) new URL(server.getURL(), path).openConnection();
    }
}
