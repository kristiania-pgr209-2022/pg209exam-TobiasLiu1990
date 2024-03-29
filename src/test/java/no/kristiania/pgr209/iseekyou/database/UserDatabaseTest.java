package no.kristiania.pgr209.iseekyou.database;

import no.kristiania.pgr209.iseekyou.InMemoryDataSource;
import no.kristiania.pgr209.iseekyou.User;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;

import static no.kristiania.pgr209.iseekyou.GenerateSampleData.sampleUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class UserDatabaseTest {

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
    void shouldNotRetrieveNonExistingUser() throws SQLException {
        assertThat(userDao.retrieve(123))
                .usingRecursiveComparison()
                .isNull();
    }

    @Test
    void shouldUpdateUser() throws SQLException {
        var originalUser = new User(100, "Original Person", "original@gmail.com", 50, "blue");
        userDao.save(originalUser);

        var update = new User(originalUser.getId(), "Updated Person", "updated@gmail.com", 100, "red");
        userDao.updateUser(update);

        var retrieveOriginalUserAfterUpdate = userDao.retrieve(originalUser.getId());


        assertNotEquals(originalUser, retrieveOriginalUserAfterUpdate);

        assertEquals(retrieveOriginalUserAfterUpdate.getFullName(), update.getFullName());
        assertEquals(retrieveOriginalUserAfterUpdate.getEmail(), update.getEmail());
        assertEquals(retrieveOriginalUserAfterUpdate.getAge(), update.getAge());
        assertEquals(retrieveOriginalUserAfterUpdate.getColor(), update.getColor());
    }

    @Test
    void shouldRetrieveAllExceptOneUser() throws SQLException {
        var currentUser = sampleUser();
        userDao.save(currentUser);

        List<User> allUsers = userDao.retrieveAll();
        int totalUsers = allUsers.size();

        assertThat(userDao.retrieve(currentUser.getId()))
                .hasNoNullFieldsOrProperties()
                .usingRecursiveComparison()
                .isEqualTo(currentUser)
                .isNotSameAs(currentUser);

        assertThat(userDao.retrieveAllUsersExceptSender(currentUser.getId()))
                .hasSizeLessThan(totalUsers);
    }

    @Test
    void shouldRetrieveAllUsersIncludingAddedUser() throws SQLException {
        List<User> userList = userDao.retrieveAll();

        var user = sampleUser();
        userDao.save(user);

        List<User> newUserList = userDao.retrieveAll();

        assertThat(userList.size())
                .isLessThan(newUserList.size());

        for (User u : newUserList) {
            assertThat(userDao.retrieve(u.getId()))
                    .hasNoNullFieldsOrProperties()
                    .isNotNull();
        }

        assertThat(userDao.retrieve(user.getId()))
                .hasNoNullFieldsOrProperties()
                .usingRecursiveComparison()
                .isEqualTo(user)
                .isNotSameAs(user);
    }

    //A bit of work around to get this test to show that its actually not possible to use the same email twice in DB.
    @Test
    void shouldFailOnDuplicateEmail() throws SQLException {
        List<String> allEmails = userDao.retrieveAllEmails();
        int numOfEmails = allEmails.size();

        for (int i = 0; i < 10; i++) {
            var thor = sampleUser();
            thor.setEmail("should@fail.com");
            userDao.save(thor);
        }

        List<String> shouldShowOneMoreEmails = userDao.retrieveAllEmails();
        int numOfEmailsPlusOne = shouldShowOneMoreEmails.size();

        assertEquals((numOfEmailsPlusOne - numOfEmails), 1);
    }
}













