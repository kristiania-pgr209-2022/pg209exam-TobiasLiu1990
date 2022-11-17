package no.kristiania.pgr209.iseekyou;

import no.kristiania.pgr209.iseekyou.database.UserDao;
import org.h2.jdbcx.JdbcDataSource;

import java.sql.SQLException;
import java.util.List;
import java.util.Random;

/**
 * SampleData is just for creating random objects of Book.
 * Easy way to see if it works as it should
 */

public class GenerateSampleData {

    private static final Random random = new Random();
    private static final JdbcDataSource dataSource = (JdbcDataSource) InMemoryDataSource.createDataSource();
    private static final UserDao userDao = new UserDao(dataSource);

    public static User sampleUser() throws SQLException {
        var user = new User();

        List<User> users = userDao.listAll();
        int id = users.size() + 1;

        user.setId(id);
        user.setFullName(pickOne("Jakob", "Nora", "Emil", "Emma", "Noah", "Ella") + " " +
                pickOne("Hansen", "Johansen", "Olsen", "Larsen", "Andersen", "Pedersen"));

        String[] lastName = user.getFullName().split(" ");
        user.setEmail(lastName[lastName.length-1] + sampleDomains());

        user.setAge(Integer.parseInt(pickOne("20", "30", "40", "50")));
        user.setColor(pickOne("red", "green", "blue", "black", "yellow", "orange", "pink", "purple"));
        return user;
    }


    private static String sampleDomains() {
        return pickOne("@gmail.com", "@gmail.no", "@hotmail.com", "@yahoo.com", "@junit.gg");
    }

    private static String pickOne(String... alternatives) {
        return alternatives[random.nextInt(alternatives.length)];
    }

}
