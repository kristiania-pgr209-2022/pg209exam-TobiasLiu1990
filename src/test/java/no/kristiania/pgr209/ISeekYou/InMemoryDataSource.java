package no.kristiania.pgr209.ISeekYou;

import org.flywaydb.core.Flyway;
import org.h2.jdbcx.JdbcDataSource;

import javax.sql.DataSource;

public class InMemoryDataSource {

    public static DataSource createDataSource() {
        var dataSource = new JdbcDataSource();
        dataSource.setUrl("jdbc:h2:mem:testDatabase;DB_CLOSE_DELAY=-1;MODE=LEGACY");
        Flyway.configure().dataSource(dataSource).load().migrate();
        return dataSource;
    }
}
