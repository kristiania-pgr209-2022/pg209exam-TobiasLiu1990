package no.kristiania.pgr209.iseekyou;

import org.flywaydb.core.Flyway;
import org.h2.jdbcx.JdbcDataSource;

import javax.sql.DataSource;

/**
 *  H2 test database
 *  Uses migration where there are some entries already. These are used for some tests
 */

public class InMemoryDataSource {

    public static DataSource createDataSource() {
        var dataSource = new JdbcDataSource();
        dataSource.setUrl("jdbc:h2:mem:testDatabase;DB_CLOSE_DELAY=-1;MODE=MSSQLServer");
        var flyway = Flyway.configure().dataSource(dataSource).load();
        flyway.migrate();
        return dataSource;
    }
}
