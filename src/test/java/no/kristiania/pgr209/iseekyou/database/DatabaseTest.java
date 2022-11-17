package no.kristiania.pgr209.iseekyou.database;

import no.kristiania.pgr209.iseekyou.InMemoryDataSource;
import no.kristiania.pgr209.iseekyou.User;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;

import static no.kristiania.pgr209.iseekyou.GenerateSampleData.sampleUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class DatabaseTest {

    private final JdbcDataSource dataSource = (JdbcDataSource) InMemoryDataSource.createDataSource();
    private final UserDao userDao = new UserDao(dataSource);

    @Test
    void shouldRetrieveSavedUser() throws SQLException {
        var user = sampleUser();
        user.setEmail("12345343" + user.getEmail());
        int id = userDao.save(user);
        user.setId(id);

        assertThat(userDao.retrieve(user.getId()))
                .hasNoNullFieldsOrProperties()
                .usingRecursiveComparison()
                .isEqualTo(user)
                .isNotSameAs(user);
    }

    @Test
    void shouldUpdateFullname() throws SQLException {
        var originalUser = sampleUser();
        int id = userDao.save(originalUser);
        originalUser.setId(id);

        assertThat(userDao.retrieve(originalUser.getId()))
                .hasNoNullFieldsOrProperties()
                .usingRecursiveComparison()
                .isEqualTo(originalUser);

        originalUser.setEmail(id + "-" + originalUser.getEmail());

        var updatedUser = new User();
        updatedUser.setFullName("A new name");
        userDao.updateUserName(updatedUser);

        assertThat(originalUser.getFullName())
                .isNotSameAs(updatedUser.getFullName());
    }

    @Test
    void shouldUpdateEmail() throws SQLException {
        var originalUser = sampleUser();
        int id = userDao.save(originalUser);
        originalUser.setId(id);

        assertThat(userDao.retrieve(originalUser.getId()))
                .hasNoNullFieldsOrProperties()
                .usingRecursiveComparison()
                .isEqualTo(originalUser);

        originalUser.setEmail(id + "-" + originalUser.getEmail());
        var updatedUser = new User();
        updatedUser.setEmail("New@mail.com");
        userDao.updateEmail(updatedUser);

        assertThat(originalUser.getEmail())
                .isNotSameAs(updatedUser.getEmail());
    }

    @Test
    void shouldUpdateAge() throws SQLException {
        var originalUser = sampleUser();
        int id = userDao.save(originalUser);
        originalUser.setId(id);

        assertThat(userDao.retrieve(originalUser.getId()))
                .hasNoNullFieldsOrProperties()
                .usingRecursiveComparison()
                .isEqualTo(originalUser);

        originalUser.setEmail(id + "-" + originalUser.getEmail());
        var updatedUser = new User();
        updatedUser.setAge(originalUser.getAge() + 1);
        userDao.updateAge(updatedUser);

        assertThat(originalUser.getAge())
                .isNotSameAs(updatedUser.getAge());
    }

    @Test
    void shouldUpdateUserFavoriteColor() throws SQLException {
        var originalUser = sampleUser();
        int id = userDao.save(originalUser);
        originalUser.setId(id);

        assertThat(userDao.retrieve(originalUser.getId()))
                .hasNoNullFieldsOrProperties()
                .usingRecursiveComparison()
                .isEqualTo(originalUser);

        originalUser.setEmail(id + "-" + originalUser.getEmail());

        var updatedUser = new User();
        updatedUser.setColor("white");  //White is not an alternative in SampleUser() for generating color.
        userDao.updateFavoriteColor(updatedUser);

        assertThat(originalUser.getColor())
                .isNotSameAs(updatedUser.getColor());
    }

    @Test
    void shouldNotBeAbleToHaveSameEmail() throws SQLException {
        var user1 = sampleUser();
        var user2 = sampleUser();
        user1.setEmail("hello@gmail.com");
        user2.setEmail("hello@gmail.com");

        try {
            userDao.save(user1);
            userDao.save(user2);
            fail("Emails are unique. Save user2 should fail");
        } catch (org.h2.jdbc.JdbcSQLIntegrityConstraintViolationException e) {
            assertNotNull(e);
        }

    }

    @Test
    void shouldReturnAllUsersAdded() throws SQLException {
        for (int i = 100; i < 105; i++) {
            User user = sampleUser();
            user.setId(i);
            user.setEmail(i + "-" + user.getEmail());
            userDao.save(user);
        }

        List<User> allUsers = userDao.listAll();

        for (User u : allUsers) {
            System.out.println(u);
        }
    }

}