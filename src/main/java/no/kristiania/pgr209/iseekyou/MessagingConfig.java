package no.kristiania.pgr209.iseekyou;

import jakarta.inject.Singleton;
import no.kristiania.pgr209.iseekyou.database.*;
import no.kristiania.pgr209.iseekyou.server.MessageEndPoint;
import org.glassfish.jersey.internal.inject.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;

import javax.sql.DataSource;

public class MessagingConfig extends ResourceConfig {

    //DataSource as parameter to constructor to be able to variable datasource (For prod / testing)
    public MessagingConfig(DataSource dataSource) {
        super(MessageEndPoint.class);

        register(new AbstractBinder() {

            @Override
            protected void configure() {
                bind(UserDao.class).to(UserDao.class).in(Singleton.class);
                bind(MessageDao.class).to(MessageDao.class).in(Singleton.class);
                bind(ConversationDao.class).to(ConversationDao.class).in(Singleton.class);
                bind(ConversationMembersDao.class).to(ConversationMembersDao.class).in(Singleton.class);
                bind(dataSource).to(DataSource.class);
            }
        });
    }
}
