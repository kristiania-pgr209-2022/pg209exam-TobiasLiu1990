package no.kristiania.pgr209.ISeekYou.database;

import com.zaxxer.hikari.HikariDataSource;

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

        return dataSource;
    }
}
