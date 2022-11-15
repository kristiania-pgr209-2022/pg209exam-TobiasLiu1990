package no.kristiania.pgr209.iseekyou.database;

import no.kristiania.pgr209.iseekyou.InMemoryDataSource;
import no.kristiania.pgr209.iseekyou.User;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

public class DatabaseTest {

    private final JdbcDataSource dataSource = (JdbcDataSource) InMemoryDataSource.createDataSource();
    private final UserDao userDao = new UserDao(dataSource);

    @Test
    void shouldRetrieveSavedUser() throws SQLException {
        var user = new User(0,"Andre Persson", "Anpe@Junit.tst", "Magenta");
        userDao.save(user);
        assertThat(userDao.retrieve(user.getId()))
                .hasNoNullFieldsOrProperties()
                .usingRecursiveComparison()
                .isEqualTo(user)
                .isNotSameAs(user);
    }

    @Test
    void shouldUpdateUserName() throws SQLException {
        var jacob = new User(0,"Jacob PleaseChangeMyName", "CrashTestDummy@Junit.tst", "Magenta");
        userDao.save(jacob);
        System.out.println(jacob.getId());
        assertThat(userDao.retrieve(jacob.getId()))
                .hasNoNullFieldsOrProperties()
                .usingRecursiveComparison()
                .isEqualTo(jacob);

        // var jacobsPreferredNewName = new User("Jacob PleaseChangeMyName", "CrashTestDummy@Junit.tst", "Magenta");;
        jacob.setFullName("Jacobs NewName");
        userDao.updateUserName(jacob);

        assertThat(userDao.retrieve(jacob.getId()))
                .hasNoNullFieldsOrProperties()
                .usingRecursiveComparison()
                .isEqualTo(jacob)
                .isNotSameAs(jacob);
    }

    @Test
    void shouldUpdateEmail() throws SQLException {
        var jacob = new User(0,"Jacob PleaseChangeMyName", "CrashTestDummy@Junit.tst", "Magenta");
        userDao.save(jacob);
        assertThat(userDao.retrieve(jacob.getId()))
                .hasNoNullFieldsOrProperties()
                .usingRecursiveComparison()
                .isEqualTo(jacob);

        // var jacobsPreferredNewName = new User("Jacob PleaseChangeMyName", "CrashTestDummy@Junit.tst", "Magenta");;
        jacob.setEmail("MmmmMmmmMmmm@Junit.tst");
        userDao.updateEmail(jacob);

        assertThat(userDao.retrieve(jacob.getId()))
                .hasNoNullFieldsOrProperties()
                .usingRecursiveComparison()
                .isEqualTo(jacob)
                .isNotSameAs(jacob);
    }

}
