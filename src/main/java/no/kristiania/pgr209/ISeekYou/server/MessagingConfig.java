package no.kristiania.pgr209.ISeekYou.server;

import org.glassfish.jersey.server.ResourceConfig;

import javax.sql.DataSource;

public class MessagingConfig extends ResourceConfig {

    public MessagingConfig(DataSource dataSource) {

    }
}
