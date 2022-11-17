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

public class UserDatabaseTestDatasource {

    private final JdbcDataSource dataSource = (JdbcDataSource) InMemoryDataSource.createDataSource();
    private final UserDao userDao = new UserDao(dataSource);

    @Test
    void shouldRetrieveSavedUser() throws SQLException {
        var user = sampleUser();
        userDao.save(user);

        assertThat(userDao.retrieve(user.getId()))
                .hasNoNullFieldsOrProperties()
                .usingRecursiveComparison()
                .isEqualTo(user)
                .isNotSameAs(user);
    }

    @Test
    void shouldRetrieveSavedUserUsingConstructor() throws SQLException {
        var user = new User(1000, "Nameless", "nej@gmail.com", "white", 100);
        userDao.save(user);

        assertThat(userDao.retrieve(user.getId()))
                .hasNoNullFieldsOrProperties()
                .usingRecursiveComparison()
                .isEqualTo(user)
                .isNotSameAs(user);
    }

    @Test
    void shouldNotRetrieveNonExistingUser() throws SQLException {
        assertThat(userDao.retrieve(123))
                .usingRecursiveComparison()
                .isNull();
    }

    @Test
    void shouldUpdateFullname() throws SQLException {
        var originalUser = sampleUser();
        userDao.save(originalUser);

        assertThat(userDao.retrieve(originalUser.getId()))
                .hasNoNullFieldsOrProperties()
                .usingRecursiveComparison()
                .isEqualTo(originalUser);

        var updatedUser = new User();
        updatedUser.setFullName("A new name");
        userDao.updateUserName(updatedUser);

        assertThat(originalUser.getFullName())
                .isNotSameAs(updatedUser.getFullName());
    }

    @Test
    void shouldUpdateEmail() throws SQLException {
        var originalUser = sampleUser();
        userDao.save(originalUser);

        assertThat(userDao.retrieve(originalUser.getId()))
                .hasNoNullFieldsOrProperties()
                .usingRecursiveComparison()
                .isEqualTo(originalUser);

        var updatedUser = new User();
        updatedUser.setEmail("New@mail.com");
        userDao.updateEmail(updatedUser);

        assertThat(originalUser.getEmail())
                .isNotSameAs(updatedUser.getEmail());
    }

    @Test
    void shouldUpdateAge() throws SQLException {
        var originalUser = sampleUser();
        userDao.save(originalUser);

        assertThat(userDao.retrieve(originalUser.getId()))
                .hasNoNullFieldsOrProperties()
                .usingRecursiveComparison()
                .isEqualTo(originalUser);

        var updatedUser = new User();
        updatedUser.setAge(originalUser.getAge() + 1);
        userDao.updateAge(updatedUser);

        assertThat(originalUser.getAge())
                .isNotSameAs(updatedUser.getAge());
    }

    @Test
    void shouldUpdateUserFavoriteColor() throws SQLException {
        var originalUser = sampleUser();
        userDao.save(originalUser);

        assertThat(userDao.retrieve(originalUser.getId()))
                .hasNoNullFieldsOrProperties()
                .usingRecursiveComparison()
                .isEqualTo(originalUser);

        var updatedUser = new User();
        updatedUser.setColor("white");  //White is not an alternative in SampleUser() for generating color.
        userDao.updateFavoriteColor(updatedUser);

        assertThat(originalUser.getColor())
                .isNotSameAs(updatedUser.getColor());
    }

    @Test
    void shouldRetrieveAllExceptOneUser() throws SQLException {
        var currentUser = sampleUser();
        userDao.save(currentUser);

        List<User> allUsers = userDao.listAll();
        int totalUsers = allUsers.size();

        assertThat(userDao.retrieve(currentUser.getId()))
                .hasNoNullFieldsOrProperties()
                .usingRecursiveComparison()
                .isEqualTo(currentUser)
                .isNotSameAs(currentUser);

        assertThat(userDao.getAllUsersExceptSender(currentUser.getId()))
                .hasSizeLessThan(totalUsers);
    }

    @Test
    void shouldRetrieveAllUsersAdded() throws SQLException {
        for (int i = 10; i < 15; i++) {
            User user = sampleUser();
            userDao.save(user);
        }

        List<User> allUsers = userDao.listAll();

        for (User u : allUsers) {
            System.out.println(u);
        }
    }

    //DB User table tests
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
    void shouldNotSaveUserWithBlankFields() throws SQLException {
        var user = new User();

        for (int i = 0; i < 4; i++) {
            user = fillFieldsToUserObject(user, i);

            if (i < 3) {
                try {
                    userDao.save(user);
                    fail("Should not be able to save user object without all fields empty");
                } catch (org.h2.jdbc.JdbcSQLIntegrityConstraintViolationException e) {
                    assertNotNull(e);
                }
            } else {
                userDao.save(user);

                assertThat(userDao.retrieve(user.getId()))
                        .hasNoNullFieldsOrProperties()
                        .usingRecursiveComparison()
                        .isEqualTo(user)
                        .isNotSameAs(user);
            }
        }
    }

    public User fillFieldsToUserObject(User user, int i) {
        switch (i) {
            case 0 -> user.setFullName("Johan Johansen");
            case 1 -> user.setEmail("Johan@gmail.com");
            case 2 -> user.setAge(30);
            case 3 -> user.setColor("red");
        }
        return user;
    }
}













