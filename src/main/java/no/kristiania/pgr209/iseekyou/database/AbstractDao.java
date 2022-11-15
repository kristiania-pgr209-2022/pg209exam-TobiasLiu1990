package no.kristiania.pgr209.iseekyou.database;

import jakarta.inject.Inject;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;

public abstract class AbstractDao <T> {
    protected final DataSource dataSource;

    @Inject
    public AbstractDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    public abstract T save(T entity) throws SQLException;
    public abstract List<T> listAll() throws SQLException;
    public abstract T retrieve(int id) throws SQLException;
}
