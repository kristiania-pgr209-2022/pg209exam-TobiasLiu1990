package no.kristiania.pgr209.iseekyou.database;

import jakarta.inject.Inject;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;

public abstract class AbstractDao <T, V> {
    protected final DataSource dataSource;

    @Inject
    public AbstractDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public abstract V save(T entity) throws SQLException;
}