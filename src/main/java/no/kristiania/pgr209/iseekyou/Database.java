package no.kristiania.pgr209.iseekyou;

import com.zaxxer.hikari.HikariDataSource;
import org.flywaydb.core.Flyway;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class Database {

    public static HikariDataSource getDatasource(){
        var dataSource = new HikariDataSource();

        String url = System.getenv("JDBC_URL");
        String username = System.getenv("JDBC_USERNAME");
        String password = System.getenv("JDBC_PASSWORD");

        if(url == null || username == null || password == null){
            var properties = new Properties();

            try(var fileReader = new FileReader("application.properties")){
                properties.load(fileReader);
            } catch (IOException e) {
                e.printStackTrace();
            }

            //Setup connection with HikariCP,
            dataSource.setJdbcUrl(properties.getProperty("jdbc.url"));
            dataSource.setUsername(properties.getProperty("jdbc.username"));
            dataSource.setPassword(properties.getProperty("jdbc.password"));
        } else {
            dataSource.setJdbcUrl(url);
            dataSource.setUsername(username);
            dataSource.setPassword(password);
        }

        Flyway.configure().dataSource(dataSource).load().migrate();
        return dataSource;
    }
}