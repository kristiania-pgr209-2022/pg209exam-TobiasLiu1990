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
        var jacob = new User(50,"Jacob PleaseChangeMyName", "Jacob@Junit.tst", "Magenta");
        userDao.save(jacob);
        System.out.println(jacob.getId());
        assertThat(userDao.retrieve(jacob.getId()))
                .hasNoNullFieldsOrProperties()
                .usingRecursiveComparison()
                .isEqualTo(jacob);

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
        var david = new User(100,"David Needs A New Email", "CrashTestDummy@Junit.tst", "Yellow");
        userDao.save(david);
        assertThat(userDao.retrieve(david.getId()))
                .hasNoNullFieldsOrProperties()
                .usingRecursiveComparison()
                .isEqualTo(david);

        david.setEmail("MmmmMmmmMmmm@Junit.tst");
        userDao.updateEmail(david);

        assertThat(userDao.retrieve(david.getId()))
                .hasNoNullFieldsOrProperties()
                .usingRecursiveComparison()
                .isEqualTo(david)
                .isNotSameAs(david);
    }

    @Test
    void shouldUpdateFavoriteColor() throws SQLException {
        var wrongColor = new User(200,"Mr Blue Sky", "ELO@Junit.tst", "Green");
        userDao.save(wrongColor);
        assertThat(userDao.retrieve(wrongColor.getId()))
                .hasNoNullFieldsOrProperties()
                .usingRecursiveComparison()
                .isEqualTo(wrongColor);

        wrongColor.setColor("Blue");
        userDao.updateFavoriteColor(wrongColor);

        assertThat(userDao.retrieve(wrongColor.getId()))
                .hasNoNullFieldsOrProperties()
                .usingRecursiveComparison()
                .isEqualTo(wrongColor)
                .isNotSameAs(wrongColor);
    }
}